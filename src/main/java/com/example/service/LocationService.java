package com.example.service;

import com.example.DTO.LocationNodeDTO;
import com.example.model.location.*;
import com.example.repo.location.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<LocationNodeDTO> buildLocationTree() {
        List<Branch> branches = getFullLocationTree();
        return branches.stream()
                .map(this::convertBranchToDTO)
                .collect(Collectors.toList());
    }

    private LocationNodeDTO convertBranchToDTO(Branch branch) {
        LocationNodeDTO dto = new LocationNodeDTO();
        dto.setId("branch_" + branch.getId());
        dto.setText(branch.getName());
        dto.setType("branch");
        dto.setChildren(convertAddressesToDTO(branch.getAddresses()));
        return dto;
    }

    private List<LocationNodeDTO> convertAddressesToDTO(List<Address> addresses) {
        if (addresses == null || addresses.isEmpty()) {
            return new ArrayList<>(); // Возвращаем пустой список, если нет адресов
        }
        return addresses.stream()
                .map(this::convertAddressToDTO)
                .collect(Collectors.toList());
    }

    private LocationNodeDTO convertAddressToDTO(Address address) {
        LocationNodeDTO dto = new LocationNodeDTO();
        dto.setId("address_" + address.getId());
        dto.setText(address.getName()); // Убедитесь, что у вас есть метод getName() в Address
        dto.setType("address");
        dto.setChildren(convertFloorsToDTO(address.getFloors()));
        return dto;
    }

    private List<LocationNodeDTO> convertFloorsToDTO(List<Floor> floors) {
        if (floors == null || floors.isEmpty()) {
            return new ArrayList<>();
        }
        return floors.stream()
                .map(this::convertFloorToDTO)
                .collect(Collectors.toList());
    }

    private LocationNodeDTO convertFloorToDTO(Floor floor) {
        LocationNodeDTO dto = new LocationNodeDTO();
        dto.setId("floor_" + floor.getId());
        dto.setText(floor.getName()); // Убедитесь, что у вас есть метод getName() в Floor
        dto.setType("floor");
        dto.setChildren(convertRoomsToDTO(floor.getRooms()));
        return dto;
    }

    private List<LocationNodeDTO> convertRoomsToDTO(List<Room> rooms) {
        if (rooms == null || rooms.isEmpty()) {
            return new ArrayList<>();
        }
        return rooms.stream()
                .map(this::convertRoomToDTO)
                .collect(Collectors.toList());
    }

    private LocationNodeDTO convertRoomToDTO(Room room) {
        LocationNodeDTO dto = new LocationNodeDTO();
        dto.setId("room_" + room.getId());
        dto.setText(room.getName()); // Убедитесь, что у вас есть метод getName() в Room
        dto.setType("room");
        dto.setChildren(convertCubiclesToDTO(room.getCubicles()));
        return dto;
    }

    private List<LocationNodeDTO> convertCubiclesToDTO(List<Cubicle> cubicles) {
        if (cubicles == null || cubicles.isEmpty()) {
            return new ArrayList<>();
        }
        return cubicles.stream()
                .map(this::convertCubicleToDTO)
                .collect(Collectors.toList());
    }

    private LocationNodeDTO convertCubicleToDTO(Cubicle cubicle) {
        LocationNodeDTO dto = new LocationNodeDTO();
        dto.setId("cubicle_" + cubicle.getId());
        dto.setText(cubicle.getName()); // Убедитесь, что у вас есть метод getName() в Cubicle
        dto.setType("cubicle");
        return dto;
    }
}
