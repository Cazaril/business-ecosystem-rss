/**
 * Revenue Settlement and Sharing System GE
 * Copyright (C) 2011-2014, Javier Lucio - lucio@tid.es
 * Telefonica Investigacion y Desarrollo, S.A.
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

package es.upm.fiware.rss.expenditureLimit.dao;

import java.util.HashMap;
import java.util.List;

import es.upm.fiware.rss.dao.GenericDao;
import es.upm.fiware.rss.expenditureLimit.model.DbeExpendLimit;
import es.upm.fiware.rss.expenditureLimit.model.DbeExpendLimitPK;
import es.upm.fiware.rss.model.BmCurrency;
import es.upm.fiware.rss.model.BmObCountry;
import es.upm.fiware.rss.model.BmService;

/**
 * 
 * 
 */
public interface DbeExpendLimitDao extends GenericDao<DbeExpendLimit, DbeExpendLimitPK> {

    public static final String NO_USER_ID = "noUserId";
    public static final String NO_APP_PROVIDER_ID = "-1";

    public static final String USER_APP_PROV_KEY = "userAndAppProvKey";
    public static final String USER_KEY = "userKey";
    public static final String APP_PROV_KEY = "appProvKey";
    public static final String ALL_GENERIC_KEY = "allGenericKey";

    /**
     * 
     * Get the limits for the user.
     * User identifier goes with service.
     * 
     * Returns the limits related to the user. Includes generic provider and system limits
     * 
     * @param urlEndUserId
     * @param aggregator
     * @param appProviderId
     * @param bmCurrency
     * @return
     */
    List<DbeExpendLimit> getExpendLimitsForUserAppProvCurrency(String urlEndUserId,
        String aggregator, String appProviderId, BmCurrency bmCurrency);

    /**
     * 
     * Get the limits for the user and grouped.
     * User identifier goes with service.
     * 
     * Returns the limits related to the user grouped by user and application provider.
     * Includes generic provider and system limits
     * 
     * @param urlEndUserId
     * @param aggregator
     * @param appProviderId
     * @param bmCurrency
     * @return
     */
    HashMap<String, List<DbeExpendLimit>> getOrdExpLimitsForUserAppProvCurrency(String urlEndUserId,
        String aggregator, String appProviderId, BmCurrency bmCurrency);

    /**
     * Get concrete limits.
     * 
     * @param aggregator
     * @param provider
     * @param userId
     * @param bmCurrency
     * @return
     */
    List<DbeExpendLimit> getExpendLimitsByProviderUserService(
            String aggregator, String provider,
            String userId, BmCurrency bmCurrency);

}
