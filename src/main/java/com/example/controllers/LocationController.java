package com.example.controllers;

import com.example.DTO.LocationNodeDTO;
import com.example.model.location.*;
import com.example.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@ResponseBody
@RequestMapping("/locations")
public class LocationController {

    @Autowired
    private LocationService locationService;



    @GetMapping("/tree")
    public ResponseEntity<List<LocationNodeDTO>> getLocationTree() {
        List<LocationNodeDTO> tree = locationService.buildLocationTree();
        return ResponseEntity.ok(tree);
    }

//    @GetMapping("/tree")
//    @ResponseBody
//    public List<Branch> getLocationTree() {
//        return locationService.getFullLocationTree();
//    }

    @GetMapping("/addresses/{branchId}")
    @ResponseBody
    public List<Address> getAddresses(@PathVariable Long branchId) {
        return locationService.getAddressesByBranch(branchId);
    }

    @GetMapping("/floors/{addressId}")
    @ResponseBody
    public List<Floor> getFloors(@PathVariable Long addressId) {
        return locationService.getFloorsByAddress(addressId);
    }

    @GetMapping("/rooms/{floorId}")
    @ResponseBody
    public List<Room> getRooms(@PathVariable Long floorId) {
        return locationService.getRoomsByFloor(floorId);
    }

    @GetMapping("/cubicles/{roomId}")
    @ResponseBody
    public List<Cubicle> getCubicles(@PathVariable Long roomId) {
        return locationService.getCubiclesByRoom(roomId);
    }


    @PostMapping("/select")
    @ResponseBody
    public ResponseEntity<String> selectLocation(@RequestParam Long id, @RequestParam String type) {

        return ResponseEntity.ok("Местоположение сохранено");
    }
}