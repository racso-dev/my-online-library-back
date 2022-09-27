package com.steamulo.helpers;

import org.springframework.data.jpa.domain.Specification;

import com.steamulo.persistence.entity.User;

public class UserSpecifications {

    private UserSpecifications() {
    }

    public static Specification<User> containsLogin(String login) {
        return (user, query, cb) -> cb.like(user.get("login"), "%" + login + "%");
    }

    public static Specification<User> containsFirstName(String firstName) {
        return (user, query, cb) -> cb.like(user.get("firstName"), "%" + firstName + "%");
    }

    public static Specification<User> containsLastName(String lastName) {
        return (user, query, cb) -> cb.like(user.get("lastName"), "%" + lastName + "%");
    }

    public static Specification<User> isActivated(Boolean activated) {
        return (user, query, cb) -> cb.equal(user.get("activated"), activated);
    }
}
