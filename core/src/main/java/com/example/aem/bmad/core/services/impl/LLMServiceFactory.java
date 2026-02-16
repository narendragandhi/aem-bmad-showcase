package com.example.aem.bmad.core.services.impl;

import com.example.aem.bmad.core.services.LLMService;
import org.osgi.service.component.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory for obtaining LLM service instances.
 * Dynamically registers available LLM providers (OpenAI, Claude, etc.)
 * and allows selection by provider ID.
 */
@Component(service = LLMServiceFactory.class)
public class LLMServiceFactory {

    private static final Logger LOG = LoggerFactory.getLogger(LLMServiceFactory.class);

    private final Map<String, LLMService> providers = new ConcurrentHashMap<>();

    @Reference(
        service = LLMService.class,
        cardinality = ReferenceCardinality.MULTIPLE,
        policy = ReferencePolicy.DYNAMIC
    )
    protected void bindLLMService(LLMService service) {
        String providerId = service.getProviderId();
        providers.put(providerId, service);
        LOG.info("Registered LLM provider: {} (available={})", providerId, service.isAvailable());
    }

    protected void unbindLLMService(LLMService service) {
        String providerId = service.getProviderId();
        providers.remove(providerId);
        LOG.info("Unregistered LLM provider: {}", providerId);
    }

    /**
     * Get an LLM service by provider ID
     *
     * @param providerId the provider (e.g., "openai", "claude")
     * @return the service, or null if not found/available
     */
    public LLMService getService(String providerId) {
        LLMService service = providers.get(providerId);
        if (service == null) {
            LOG.warn("LLM provider not found: {}", providerId);
            return null;
        }
        if (!service.isAvailable()) {
            LOG.warn("LLM provider not available: {}", providerId);
            return null;
        }
        return service;
    }

    /**
     * Get the first available LLM service
     *
     * @return an available service, or null if none configured
     */
    public LLMService getAvailableService() {
        return providers.values().stream()
            .filter(LLMService::isAvailable)
            .findFirst()
            .orElse(null);
    }

    /**
     * Check if any LLM service is available
     */
    public boolean hasAvailableService() {
        return providers.values().stream().anyMatch(LLMService::isAvailable);
    }

    /**
     * Get all registered provider IDs
     */
    public java.util.Set<String> getProviderIds() {
        return providers.keySet();
    }
}
