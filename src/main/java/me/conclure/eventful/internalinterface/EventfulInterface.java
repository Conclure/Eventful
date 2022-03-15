package me.conclure.eventful.internalinterface;

import me.conclure.eventful.model.Repository;
import me.conclure.eventful.model.User;

import java.util.UUID;

public interface EventfulInterface {
    UserManagerInterface userManager();
}