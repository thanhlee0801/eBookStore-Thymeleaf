package com.vn.ebookstore.service;

import com.vn.ebookstore.model.Address;
import java.util.List;

public interface AddressService {
    Address createAddress(Address address);
    Address updateAddress(int id, Address address);
    void deleteAddress(int id);
    Address getAddressById(int id);
    List<Address> getAllAddresses();
    List<Address> getAddressesByUserId(int userId);
    void softDeleteAddress(int id);
}