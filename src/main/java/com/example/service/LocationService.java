package com.example.service;

import com.example.model.location.*;
import com.example.repo.location.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationService {

    @Autowired
    private BranchRepository branchRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private FloorRepository floorRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private CubicleRepository cubicleRepository;

    public List<Branch> getFullLocationTree() {
        return branchRepository.findAll();
    }

    public List<Address> getAddressesByBranch(Long branchId) {
        return addressRepository.findByBranchId(branchId);
    }

    public List<Floor> getFloorsByAddress(Long addressId) {
        return floorRepository.findByAddressId(addressId);
    }

    public List<Room> getRoomsByFloor(Long floorId) {
        return roomRepository.findByFloorId(floorId);
    }

    public List<Cubicle> getCubiclesByRoom(Long roomId) {
        return cubicleRepository.findByRoomId(roomId);
    }
}