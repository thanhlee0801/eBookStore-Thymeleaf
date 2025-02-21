package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Address;
import com.vn.ebookstore.repository.AddressRepository;
import com.vn.ebookstore.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address createAddress(Address address) {
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(int id, Address address) {
        Address existingAddress = addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
        existingAddress.setTitle(address.getTitle());
        existingAddress.setAddressLine1(address.getAddressLine1());
        existingAddress.setAddressLine2(address.getAddressLine2());
        existingAddress.setCountry(address.getCountry());
        existingAddress.setCity(address.getCity());
        existingAddress.setPostalCode(address.getPostalCode());
        existingAddress.setLandmark(address.getLandmark());
        existingAddress.setPhoneNumber(address.getPhoneNumber());
        return addressRepository.save(existingAddress);
    }

    @Override
    public void deleteAddress(int id) {
        addressRepository.deleteById(id);
    }

    @Override
    public Address getAddressById(int id) {
        return addressRepository.findById(id).orElseThrow(() -> new RuntimeException("Address not found"));
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }
}