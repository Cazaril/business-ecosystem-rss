/**
 * Copyright (C) 2015, CoNWeT Lab., Universidad Politécnica de Madrid
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package es.upm.fiware.rss.service;

import es.upm.fiware.rss.dao.DbeAggregatorDao;
import es.upm.fiware.rss.dao.DbeAppProviderDao;
import es.upm.fiware.rss.exception.RSSException;
import es.upm.fiware.rss.exception.UNICAExceptionType;
import es.upm.fiware.rss.model.DbeAggregator;
import es.upm.fiware.rss.model.DbeAppProvider;
import es.upm.fiware.rss.model.DbeAppProviderId;
import es.upm.fiware.rss.model.RSSProvider;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author fdelavega
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ProviderManager {
    /***
     * Logging system.
     */
    private final Logger logger = LoggerFactory.getLogger(ProviderManager.class);

    /**
     * 
     */
    @Autowired
    private DbeAppProviderDao appProviderDao;

    @Autowired
    private DbeAggregatorDao aggregatorDao;

    @Autowired
    private RSSModelsManager modelsManager;


    private RSSProvider getAPIModel(DbeAppProvider p) {
        RSSProvider apiProvider = new RSSProvider();

        apiProvider.setAggregatorId(p.getId().getAggregator().getTxEmail());
        apiProvider.setProviderId(p.getId().getTxAppProviderId());
        apiProvider.setProviderName(p.getTxName());
        return apiProvider;
    }

    /**
     * Get providers from the DB in a format ready to be serialized
     * @param aggregatorId
     * @return
     * @throws RSSException 
     */
    public List<RSSProvider> getAPIProviders(String aggregatorId) throws RSSException {
        List<RSSProvider> apiProviders = new ArrayList<>();
        List<DbeAppProvider> providers = this.getProviders(aggregatorId);

        for(DbeAppProvider p: providers) {
            RSSProvider apiProvider = this.getAPIModel(p);
            apiProviders.add(apiProvider);
        }
        return apiProviders;
    }

    /**
     * Builds the provider model identified by an aggregator and a provider id
     * @param aggregatorId, id of the given aggregator
     * @param providerId, id of the given provider within the given aggregator
     * @throws RSSException, if the provided information is not valid
     * @return RSSProvider instance with the info of the identified provider
     */
    public RSSProvider getProvider(String aggregatorId,
            String providerId) throws RSSException {

        // Validate ids
        this.modelsManager.checkValidAppProvider(aggregatorId, providerId);

        DbeAppProvider provModel = this.appProviderDao.getProvider(aggregatorId, providerId);

        if (provModel == null) {
            String[] args = {aggregatorId + " " + providerId};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

        return this.getAPIModel(provModel);
    }

    /**
     * Get providers from bbdd.
     * 
     * @param aggregatorId
     * @return
     * @throws RSSException
     */
    public List<DbeAppProvider> getProviders(String aggregatorId) throws RSSException {
        List<DbeAppProvider> providers;

        if (null != aggregatorId && !aggregatorId.isEmpty()) {
            providers = this.appProviderDao.getProvidersByAggregator(aggregatorId);
        } else {
            providers = this.appProviderDao.getAll();
        }

        if (providers == null) {
            providers = new ArrayList<>();
        }

        return providers;
    }

    /**
     * Create a new provider for a given aggregator.
     * 
     * @param providerId
     * @param providerName
     * @param aggregatorId
     * @throws RSSException
     */
    public void createProvider(String providerId, String providerName,
            String aggregatorId) throws RSSException {

        logger.debug("Creating provider: {}", providerId);

        // Validate required fields
        if (providerId == null || providerId.isEmpty()) {
            String[] args = {"ProviderID field is required for creating a provider"};
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }

        if (providerName == null || providerName.isEmpty()) {
            String[] args = {"ProviderName field is required for creating a provider"};
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }

        if (aggregatorId == null || aggregatorId.isEmpty()) {
            String[] args = {"AggregatorID field is required for creating a provider"};
            throw new RSSException(UNICAExceptionType.MISSING_MANDATORY_PARAMETER, args);
        }

        // Check that the aggregator exists
        DbeAggregator aggregator = this.aggregatorDao.getById(aggregatorId);
        if (aggregator == null) {
            String[] args = {"The given aggregator does not exists"};
            throw new RSSException(UNICAExceptionType.NON_EXISTENT_RESOURCE_ID, args);
        }

        DbeAppProvider provModel = this.appProviderDao.getProvider(aggregatorId, providerId);

        if (provModel != null) {
            String[] args = {"The provider " + providerId + " of the aggregator " + aggregatorId + " already exists"};
            throw new RSSException(UNICAExceptionType.RESOURCE_ALREADY_EXISTS, args);
        }

        // Build provider ID
        DbeAppProviderId id = new DbeAppProviderId();
        id.setTxAppProviderId(providerId);
        id.setAggregator(aggregator);

        // Build new Provider entity
        DbeAppProvider provider = new DbeAppProvider();
        provider.setId(id);
        provider.setTxName(providerName);
        provider.setTxCorrelationNumber(0);
        provider.setTxTimeStamp(new Date());

        // Create provider
        appProviderDao.create(provider);
    }
}
