package tn.anasazx.tunirate.invitation.service.implementation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
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
import tn.anasazx.tunirate.security.SecurityUtils;
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
    public void sendInvitation(CompanyInvitationRequest request) {

        Long inviterId = SecurityUtils.getCurrentUserId();
        String email = request.getInvitedUserEmail();

        log.info("INVITATION_ATTEMPT inviterId={} email={}", inviterId, email);

        CompanyMemberResponse membership = companyMemberService.getCompanyByUserId(inviterId);

        if (membership == null) {
            log.warn("INVITATION_BLOCKED: user {} not in any company", inviterId);
            return;
        }

        Long companyId = membership.companyId();

        Optional<Company> companyOpt = companyRepository.findById(companyId);
        Optional<User> inviterOpt = userRepository.findById(inviterId);
        Optional<User> invitedUserOpt = userRepository.findByEmail(email);

        if (companyOpt.isEmpty() || inviterOpt.isEmpty() || invitedUserOpt.isEmpty()) {
            log.warn("INVITATION_BLOCKED: missing data company={}, inviter={}, invitedEmail={}",
                    companyId, inviterId, email);
            return;
        }

        Company company = companyOpt.get();
        User inviter = inviterOpt.get();
        User invitedUser = invitedUserOpt.get();

        if (inviter.getEmail().equals(email)) {
            log.warn("INVITATION_BLOCKED: self-invitation inviterId={}", inviterId);
            return;
        }

        boolean exists = invitationRepository.existsByCompanyAndUserAndStatusIn(
                company,
                invitedUser,
                List.of(InvitationStatus.PENDING)
        );

        if (exists) {
            log.info("INVITATION_BLOCKED: already exists companyId={} userId={}",
                    companyId, invitedUser.getId());
            return;
        }

        if (companyMemberService.isUserInCompany(invitedUser.getId(), companyId)) {
            log.info("INVITATION_BLOCKED: user already member userId={} companyId={}",
                    invitedUser.getId(), companyId);
            return;
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

    @Override
    @Transactional
    public CompanyInvitationResponse acceptInvitation(Long invitationId) {

        Long userId = SecurityUtils.getCurrentUserId();

        CompanyInvitation invitation = invitationRepository
                .findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        if (!invitation.getUser().getId().equals(userId)) throw new RuntimeException("Not allowed");

        if (invitation.getStatus() != InvitationStatus.PENDING) throw new RuntimeException("Invitation is not pending");

        if (invitation.isExpired()) throw new RuntimeException("Invitation expired");

        companyMemberService.assignUserToCompany(invitation.getUser().getId(), invitation.getCompany().getId());

        invitation.setStatus(InvitationStatus.ACCEPTED);

        invitationRepository.save(invitation);

        return CompanyInvitationMapper.toResponse(invitation);
    }

    @Override
    @Transactional
    public CompanyInvitationResponse rejectInvitation(Long invitationId) {

        Long userId = SecurityUtils.getCurrentUserId();

        CompanyInvitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        // 1. Security check
        if (!invitation.getUser().getId().equals(userId)) {
            throw new RuntimeException("Not allowed");
        }

        // 2. Expiration check
        if (invitation.isExpired()) {
            throw new RuntimeException("Invitation expired");
        }

        // 3. State check
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new RuntimeException("Invitation is not pending");
        }

        // 4. Apply rejection
        invitation.setStatus(InvitationStatus.REJECTED);
        invitationRepository.save(invitation);
        return CompanyInvitationMapper.toResponse(invitation);
    }

    @Override
    @Transactional
    public CompanyInvitationResponse cancelInvitation(Long invitationId) {

        Long userId = SecurityUtils.getCurrentUserId();

        CompanyInvitation invitation = invitationRepository.findById(invitationId)
                .orElseThrow(() -> new RuntimeException("Invitation not found"));

        Long companyId = invitation.getCompany().getId();

        // 1. Verify caller belongs to company (or is allowed role)
        if (!companyMemberService.isUserHeadInCompany(userId, companyId)) {
            throw new RuntimeException("Not allowed");
        }


        // 2. Optional but recommended: prevent canceling non-pending invitations
        if (invitation.getStatus() != InvitationStatus.PENDING) {
            throw new RuntimeException("Only pending invitations can be cancelled");
        }

        // 3. Apply cancel
        invitation.setStatus(InvitationStatus.CANCELLED);
        invitationRepository.save(invitation);
        return CompanyInvitationMapper.toResponse(invitation);

    }

    @Override
    public List<CompanyInvitationResponse> getCompanyInvitations() {

        Long userId = SecurityUtils.getCurrentUserId();

        CompanyMemberResponse membership = companyMemberService.getCompanyByUserId(userId);

        if (membership == null) {
            throw new RuntimeException("User is not part of any company");
        }

        Long companyId = membership.companyId();

        if (membership.companyRole() != CompanyRole.HEAD) {
            throw new RuntimeException("Not allowed");
        }

        return invitationRepository.findAllByCompanyId(companyId)
                .stream()
                .map(CompanyInvitationMapper::toResponse)
                .toList();
    }

    @Override
    public List<CompanyInvitationResponse> getUserInvitations() {

        Long userId = SecurityUtils.getCurrentUserId();

        return invitationRepository.findAllByUserIdAndStatusInOrderByCreatedAtDesc(userId, USER_VISIBLE_STATUSES)
                .stream()
                .map(CompanyInvitationMapper::toResponse)
                .toList();
    }

}