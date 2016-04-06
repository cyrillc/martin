package ch.zhaw.psit4.martin.common.dao;

import org.junit.Before;
import org.junit.Test;

import ch.zhaw.psit4.martin.common.HistoryItem;
import ch.zhaw.psit4.martin.common.Request;
import ch.zhaw.psit4.martin.common.Response;

public class HistoryItemDAOTest {

    public HistoryItemDAO historyItemDAO;

    @Before
    public void setUp() {
        this.historyItemDAO = new HistoryItemDAO();
    }

}
