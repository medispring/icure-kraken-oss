/*
 * Copyright (C) 2018 Taktik SA
 *
 * This file is part of iCureBackend.
 *
 * iCureBackend is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2 as published by
 * the Free Software Foundation.
 *
 * iCureBackend is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with iCureBackend.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.taktik.icure.dao.impl;

import org.ektorp.ComplexKey;
import org.ektorp.ViewQuery;
import org.ektorp.support.View;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;
import org.taktik.icure.dao.TimeTableDAO;
import org.taktik.icure.dao.impl.ektorp.CouchDbICureConnector;
import org.taktik.icure.dao.impl.idgenerators.IDGenerator;
import org.taktik.icure.entities.TimeTable;

import java.util.List;

@Repository("timeTableDAO")
@View(name = "all", map = "function(doc) { if (doc.java_type == 'org.taktik.icure.entities.TimeTable' && !doc.deleted) emit( null, doc._id )}")
public class TimeTableDAOImpl extends GenericDAOImpl<TimeTable> implements TimeTableDAO {

    @Autowired
    public TimeTableDAOImpl(@SuppressWarnings("SpringJavaAutowiringInspection") @Qualifier("couchdbHealthdata") CouchDbICureConnector db, IDGenerator idGenerator) {
        super(TimeTable.class, db, idGenerator);
        initStandardDesignDocument();
    }

    @Override
    @View(name = "by_hcparty_and_startdate", map = "classpath:js/timeTable/by_hcparty_and_startdate.js")
    public List<TimeTable> listTimeTableByPeriodAndHcPartyId(Long startDate, Long endDate, String hcPartyId) {
        ComplexKey from = ComplexKey.of(
                hcPartyId,
                startDate
        );
        ComplexKey to = ComplexKey.of(
                hcPartyId,
                endDate == null ? ComplexKey.emptyObject() : endDate
        );

        ViewQuery viewQuery = createQuery("by_hcparty_and_startdate")
                .startKey(from)
                .endKey(to)
                .includeDocs(false);

        List<TimeTable> timeTables = db.queryView(viewQuery, TimeTable.class);
        return timeTables;
    }
}
