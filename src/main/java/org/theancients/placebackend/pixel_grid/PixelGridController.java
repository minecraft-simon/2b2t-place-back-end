package org.theancients.placebackend.pixel_grid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.RolesAllowed;
import java.security.Principal;

@RestController
@RequestMapping("/pixels")
public class PixelGridController {

    @Autowired
    private PixelGridService pixelGridService;

    @PutMapping
    @RolesAllowed("ROLE_AUTHENTICATED_USER")
    public ResponseEntity<PixelUpdateResponseDto> updatePixel(Principal principal, @RequestBody PixelDto pixelDto) {
        return pixelGridService.updatePixel(principal.getName(), pixelDto);
    }

}
