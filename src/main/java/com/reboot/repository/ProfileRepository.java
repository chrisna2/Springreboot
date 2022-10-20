package com.reboot.repository;

import org.springframework.data.repository.CrudRepository;

import com.reboot.domain.Profile;

public interface ProfileRepository extends CrudRepository<Profile, Long> {

}
