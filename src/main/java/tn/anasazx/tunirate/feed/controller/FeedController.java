package tn.anasazx.tunirate.feed.controller;


import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.anasazx.tunirate.feed.dto.FeedResponse;
import tn.anasazx.tunirate.feed.service.FeedService;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @GetMapping
    public FeedResponse getFeed() {
        return feedService.getFeed();
    }
}