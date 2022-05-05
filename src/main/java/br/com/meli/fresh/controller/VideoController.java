package br.com.meli.fresh.controller;

import br.com.meli.fresh.assembler.VideoMapper;
import br.com.meli.fresh.dto.request.VideoRequest;
import br.com.meli.fresh.dto.request.VideoStatusRequest;
import br.com.meli.fresh.dto.response.VideoResponse;
import br.com.meli.fresh.model.Video;
import br.com.meli.fresh.services.impl.VideoServiceImpl;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/v1/fresh-products/videos")
@AllArgsConstructor
public class VideoController {

    private final VideoServiceImpl videoService;
    private final VideoMapper mapper;

    @ApiOperation(value = "This endpoint sends a user video.")
    @PostMapping()
    public ResponseEntity<String> createByUser(@Valid @RequestBody VideoRequest request, UriComponentsBuilder uriBuilder) {
        Video video = videoService.createByBuyer(mapper.toDomainObject(request));

        URI uri = uriBuilder
                .path("/{id}")
                .buildAndExpand(video.getId())
                .toUri();

        return ResponseEntity.created(uri).body("Video created successfully.");
    }

    @ApiOperation(value = "This endpoint get all approved videos for a product.")
    @GetMapping("product/{productId}")
    public ResponseEntity<Page<VideoResponse>> getAllByProduct(@PathVariable String productId,
                                                               Pageable pageable) {
        Page<Video> videos = videoService.getAllByProduct(productId, pageable);
        List<VideoResponse> list = videos.stream().map(video -> mapper.toResponseObject(video)).collect(Collectors.toList());
        Page<VideoResponse> pageList = new PageImpl<>(list);
        return ResponseEntity.ok(pageList);
    }

    @ApiOperation(value = "This endpoint get all videos from a seller.")
    @GetMapping()
    public ResponseEntity<Page<VideoResponse>> getAllBySeller(@RequestParam(required = false) Boolean status, Pageable pageable) {
        boolean approved = true;

        if (status !=  null && !status) {
            approved = false;
        }
        Page<Video> videos = videoService.getAllBySeller(approved, pageable);
        List<VideoResponse> list = videos.stream().map(video -> mapper.toResponseObject(video)).collect(Collectors.toList());
        Page<VideoResponse> pageList = new PageImpl<>(list);
        return ResponseEntity.ok(pageList);
    }

    @ApiOperation(value = "This endpoint is used to change the approval status of a video.")
    @PutMapping("{id}")
    public ResponseEntity<String> changeApprovalVideo(@PathVariable String id, @Valid @RequestBody VideoStatusRequest request) {
       videoService.changeApproval(id, request.getStatus());
        return ResponseEntity.ok("Changed the video status to " + request.getStatus().toString());
    }

    @ApiOperation(value = "This endpoint delete a video by video id.")
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteVideo(@PathVariable String id) {
        videoService.deleteById(id);
        return ResponseEntity.ok("Video successfully deleted.");
    }
}