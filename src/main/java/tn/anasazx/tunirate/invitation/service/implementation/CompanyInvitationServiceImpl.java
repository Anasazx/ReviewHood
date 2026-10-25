package tn.anasazx.tunirate.invitation.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.transaction.annotation.Transactional;
import tn.anasazx.tunirate.company.entity.Company;
import tn.anasazx.tunirate.company.repository.CompanyRepository;
import tn.anasazx.tunirate.enums.CompanyRole;
import tn.anasazx.tunirate.enums.InvitationStatus;
import tn.anasazx.tunirate.invitation.dto.CompanyInvitationRequest;
import tn.anasazx.tunirate.invitation.dto.CompanyInvitationResponse;
import tn.anasazx.tunirate.invitation.entity.CompanyInvitation;
import tn.anasazx.tunirate.invitation.mapper.CompanyInvitationMapper;
import tn.anasazx.tunirate.invitation.repository.CompanyInvitationRepository;
import tn.anasazx.tunirate.invitation.service.CompanyInvitationService;
import tn.anasazx.tunirate.membership.dto.CompanyMemberResponse;
import tn.anasazx.tunirate.membership.service.CompanyMemberService;
import tn.anasazx.tunirate.user.entity.User;
import tn.anasazx.tunirate.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CompanyInvitationServiceImpl implements CompanyInvitationService {

    private final CompanyInvitationRepository invitationRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;
    private final CompanyMemberService companyMemberService;
    private static final List<InvitationStatus> USER_VISIBLE_STATUSES =  List.of(InvitationStatus.PENDING, InvitationStatus.ACCEPTED, InvitationStatus.REJECTED);

    @Override
    public void sendInvitation(CompanyInvitationRequest request, Long currentUserId) {

        String email = request.getInvitedUserEmail() == null ? "" : request.getInvitedUserEmail().trim();

        log.info("INVITATION_ATTEMPT inviterId={} email={}", currentUserId, email);

        CompanyMemberResponse membership = companyMemberService.getCompanyByUserId(currentUserId);

        if (membership == null || membership.companyRole() != CompanyRole.HEAD) {
            log.warn("INVITATION_BLOCKED: user {} is not a company head", currentUserId);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only the company head can invite people");
        }

        Long companyId = membership.companyId();

        Optional<Company> companyOpt = companyRepository.findById(companyId);
        Optional<User> inviterOpt = userRepository.findById(currentUserId);
        Optional<User> invitedUserOpt = userRepository.findByEmail(email);

        if (companyOpt.isEmpty() || inviterOpt.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Company not found");
        }
        if (invitedUserOpt.isEmpty()) {
            log.info("INVITATION_BLOCKED: no account for invited email, companyId={}", companyId);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No ReviewHood account uses this email. Ask them to sign up first, then invite them.");
        }

        Company company = companyOpt.get();
        User inviter = inviterOpt.get();
        User invitedUser = invitedUserOpt.get();

        if (inviter.getEmail().equalsIgnoreCase(email)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You can't invite yourself");
        }

        boolean exists = invitationRepository.existsByCompanyAndUserAndStatusIn(
                company,
                invitedUser,
                List.of(InvitationStatus.PENDING)
        );

        if (exists) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This person already has a pending invitation");
        }

        if (companyMemberService.isUserInCompany(invitedUser.getId(), companyId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "This person is already in your team");
        }

        CompanyInvitation invitation = CompanyInvitation.builder()
                .company(company)
                .user(invitedUser)
                .invitedBy(inviter)
                .status(InvitationStatus.PENDING)
                .expiresAt(LocalDateTime.now().plusDays(7))
                .build();

        invitationRepository.save(invitation);

        log.info("INVITATION_CREATED companyId={} invitedUserId={}",
                companyId, invitedUser.getId());
    }

    @Transactional
    @Override
    public CompanyInvitationResponse acceptInvitation(Long invitationId, Long currentUserId) {

        CompanyInvitation invitation = invitationRepository
                .findById(invitationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not found"));

        if (!invitation.getUser().getId().equals(currentUserId)) throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed");

        if (invitation.getStatus() != InvitationStatus.PENDING) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invitation is not pending");

        if (invitation.isExpired()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invitation expired");

        companyMemberService.assignUserToCompany(invitation.getUser().getId(), invitation.getCompany().getId());

        invitation.setStatus(InvitationStatus.ACCEPTED);

        invitationRepository.save(invitation);

        return CompanyInvitationMapper.toResponse(invitation);
    }

    @Transactional
    @Override
    public CompanyInvitationResponse rejectInvitation(Long invitationId, Long currentUserId) {

        CompanyInvitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not found"));

        // 1. Security check
        if (!invitation.getUser().getId().equals(currentUserId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed");
        }

        // 2. Expiration check
        if (invitation.isExpired()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invitation expired");
        }

        // 3. State check
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invitation is not pending");
        }

        // 4. Apply rejection
        invitation.setStatus(InvitationStatus.REJECTED);
        invitationRepository.save(invitation);
        return CompanyInvitationMapper.toResponse(invitation);
    }

    @Transactional
    @Override
    public CompanyInvitationResponse cancelInvitation(Long invitationId, Long currentUserId) {

        CompanyInvitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Invitation not found"));

        Long companyId = invitation.getCompany().getId();

        // 1. Verify caller belongs to company (or is allowed role)
        if (!companyMemberService.isUserHeadInCompany(currentUserId, companyId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed");
        }


        // 2. Optional but recommended: prevent canceling non-pending invitations
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only pending invitations can be cancelled");
        }

        // 3. Apply cancel
        invitation.setStatus(InvitationStatus.CANCELLED);
        invitationRepository.save(invitation);
        return CompanyInvitationMapper.toResponse(invitation);

    }

    @Override
    public List<CompanyInvitationResponse> getCompanyInvitations(Long currentUserId) {

        CompanyMemberResponse membership = companyMemberService.getCompanyByUserId(currentUserId);

        if (membership == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "User is not part of any company");
        }

        Long companyId = membership.companyId();

        if (membership.companyRole() != CompanyRole.HEAD) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not allowed");
        }

        return invitationRepository.findAllByCompanyId(companyId)
                .stream()
                .map(CompanyInvitationMapper::toResponse)
                .toList();
    }

    @Override
    public List<CompanyInvitationResponse> getUserInvitations(Long currentUserId) {
        return invitationRepository.findAllByUserIdAndStatusInOrderByCreatedAtDesc(currentUserId, USER_VISIBLE_STATUSES)
                .stream()
                .map(CompanyInvitationMapper::toResponse)
                .toList();
    }

}