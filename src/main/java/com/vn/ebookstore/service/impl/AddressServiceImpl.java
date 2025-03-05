package com.vn.ebookstore.service.impl;

import com.vn.ebookstore.model.Address;
import com.vn.ebookstore.repository.AddressRepository;
import com.vn.ebookstore.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address createAddress(Address address) {
        address.setCreatedAt(new Date());
        return addressRepository.save(address);
    }

    @Override
    public Address updateAddress(int id, Address address) {
        Address existingAddress = addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
        
        existingAddress.setAddressLine(address.getAddressLine());
        existingAddress.setWard(address.getWard());
        existingAddress.setDistrict(address.getDistrict());
        existingAddress.setCity(address.getCity());
        existingAddress.setCountry(address.getCountry());
        existingAddress.setPostalCode(address.getPostalCode());
        
        return addressRepository.save(existingAddress);
    }

    @Override
    public void deleteAddress(int id) {
        addressRepository.deleteById(id);
    }

    @Override
    public Address getAddressById(int id) {
        return addressRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ"));
    }

    @Override
    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public List<Address> getAddressesByUserId(int userId) {
        return addressRepository.findByUserIdAndDeletedAtIsNull(userId);
    }

    @Override
    public void softDeleteAddress(int id) {
        Address address = getAddressById(id);
        address.setDeletedAt(new Date());
        addressRepository.save(address);
    }
}