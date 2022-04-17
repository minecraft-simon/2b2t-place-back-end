package org.theancients.placebackend.pixel_grid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pixel-grid")
public class PixelGridController {

    @Autowired
    private PixelGridService pixelGridService;

    @PutMapping
    public ResponseEntity<PixelDto> updatePixel(@RequestBody PixelDto pixelDto) {
        HttpStatus httpStatus = pixelGridService.updatePixel(pixelDto);
        return ResponseEntity.status(httpStatus).body(pixelDto);
    }

}
