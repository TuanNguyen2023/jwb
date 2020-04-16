/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gcs.wb.bapi;

import com.gcs.wb.bapi.goodsmvt.GoodsMvtCancelBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtPoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtDoCreateBapi;
import com.gcs.wb.bapi.goodsmvt.GoodsMvtPOSTOCreatePGIBapi;
import com.gcs.wb.bapi.goodsmvt.SOGetDetailBapi;
import com.gcs.wb.bapi.helper.BOMReadBapi;
import com.gcs.wb.bapi.helper.BatchStocksGetListBapi;
import com.gcs.wb.bapi.helper.CheckVersionWBBapi;
import com.gcs.wb.bapi.helper.CustomerGetDetailBapi;
import com.gcs.wb.bapi.helper.CustomerGetListBapi;
import com.gcs.wb.bapi.helper.DoGetDetailBapi;
import com.gcs.wb.bapi.helper.MatAvailableBapi;
import com.gcs.wb.bapi.helper.MatGetDetailBapi;
import com.gcs.wb.bapi.helper.MatLookupBapi;
import com.gcs.wb.bapi.helper.MaterialGetListBapi;
import com.gcs.wb.bapi.helper.MvtGetDetailBapi;
import com.gcs.wb.bapi.helper.MvtReasonsGetListBapi;
import com.gcs.wb.bapi.helper.PlantGetDetailBapi;
import com.gcs.wb.bapi.helper.PoGetDetailBapi;
import com.gcs.wb.bapi.helper.PoPostGetListBapi;
import com.gcs.wb.bapi.helper.SLocsGetListBapi;
import com.gcs.wb.bapi.helper.SaleOrderGetDetailBapi;
import com.gcs.wb.bapi.helper.StoGetListBapi;
import com.gcs.wb.bapi.helper.SyncContractSOGetListBapi;
import com.gcs.wb.bapi.helper.TransportagentGetListBapi;
import com.gcs.wb.bapi.helper.UnitsGetListBapi;
import com.gcs.wb.bapi.helper.UserGetDetailBapi;
import com.gcs.wb.bapi.helper.VendorGetDetailBapi;
import com.gcs.wb.bapi.helper.VendorValiationCheckBapi;
import com.gcs.wb.bapi.outbdlv.DOCreate2PGIBapi;
import com.gcs.wb.bapi.outbdlv.DOPostingPGIBapi;
import com.gcs.wb.bapi.outbdlv.DORevertBapi;
import com.gcs.wb.bapi.outbdlv.OutbDeliveryCreateStoBapi;
import com.gcs.wb.bapi.outbdlv.WsDeliveryUpdateBapi;
import com.gcs.wb.bapi.text.TextCreateBapi;
import com.gcs.wb.bapi.text.TextReadBapi;
import com.gcs.wb.bapi.text.TextSaveBapi;
import com.gcs.wb.jpa.entity.Configuration;
import com.gcs.wb.model.AppConfig;
import com.sap.conn.jco.ext.DestinationDataProvider;
import org.hibersap.configuration.AnnotationConfiguration;
import org.hibersap.configuration.xml.SessionManagerConfig;
import org.hibersap.execution.jco.JCoContext;
import org.hibersap.session.Credentials;
import org.hibersap.session.SessionManager;

/**
 *
 * @author Tran-Vu
 */
public class BAPIConfiguration {

    /**
     *
     * @param config
     * @param credential 
     * @return
     */
    public static SessionManager getSessionManager(AppConfig config, Credentials credential) {
        SessionManagerConfig sessionConfig = new SessionManagerConfig("tafico");
        Configuration configuration = config.getConfiguration();

        sessionConfig.setContext(JCoContext.class.getName());
        sessionConfig.setProperty(DestinationDataProvider.JCO_ASHOST, configuration.getSapGwHost());
        if (configuration.getSapGwHost() != null && !configuration.getSapGwHost().isEmpty()) {
            sessionConfig.setProperty(DestinationDataProvider.JCO_GWHOST, configuration.getSapGwHost());
        }        
        sessionConfig.setProperty(DestinationDataProvider.JCO_SYSNR, configuration.getSapSystemNumber());
        sessionConfig.setProperty(DestinationDataProvider.JCO_CLIENT, configuration.getSapClient());
        sessionConfig.setProperty(DestinationDataProvider.JCO_USER, credential.getUser());
        sessionConfig.setProperty(DestinationDataProvider.JCO_PASSWD, credential.getPassword());
//        sessionConfig.setProperty(DestinationDataProvider.JCO_LANG, "EN");
        if (configuration.getSapRouteString() != null && !configuration.getSapRouteString().isEmpty()) {
            sessionConfig.setProperty(DestinationDataProvider.JCO_SAPROUTER, configuration.getSapRouteString());
        }

        sessionConfig.addAnnotatedClass(TextCreateBapi.class);
        sessionConfig.addAnnotatedClass(TextReadBapi.class);
        sessionConfig.addAnnotatedClass(TextSaveBapi.class);
        sessionConfig.addAnnotatedClass(OutbDeliveryCreateStoBapi.class);
        sessionConfig.addAnnotatedClass(WsDeliveryUpdateBapi.class);
        sessionConfig.addAnnotatedClass(GoodsMvtCancelBapi.class);
        sessionConfig.addAnnotatedClass(GoodsMvtPoCreateBapi.class);
        sessionConfig.addAnnotatedClass(GoodsMvtDoCreateBapi.class);
        sessionConfig.addAnnotatedClass(BatchStocksGetListBapi.class);
        sessionConfig.addAnnotatedClass(MvtGetDetailBapi.class);
        sessionConfig.addAnnotatedClass(MvtReasonsGetListBapi.class);
        sessionConfig.addAnnotatedClass(PlantGetDetailBapi.class);
        sessionConfig.addAnnotatedClass(PoGetDetailBapi.class);
        sessionConfig.addAnnotatedClass(SLocsGetListBapi.class);
        sessionConfig.addAnnotatedClass(StoGetListBapi.class);
        sessionConfig.addAnnotatedClass(DoGetDetailBapi.class);
        sessionConfig.addAnnotatedClass(UnitsGetListBapi.class);
        sessionConfig.addAnnotatedClass(UserGetDetailBapi.class);
        sessionConfig.addAnnotatedClass(DOCreate2PGIBapi.class);
        sessionConfig.addAnnotatedClass(CustomerGetDetailBapi.class);
        sessionConfig.addAnnotatedClass(VendorGetDetailBapi.class);
        sessionConfig.addAnnotatedClass(MatGetDetailBapi.class);
        sessionConfig.addAnnotatedClass(BOMReadBapi.class);
        sessionConfig.addAnnotatedClass(MatAvailableBapi.class);
        sessionConfig.addAnnotatedClass(MatLookupBapi.class);
        sessionConfig.addAnnotatedClass(DORevertBapi.class);
        sessionConfig.addAnnotatedClass(CheckVersionWBBapi.class);
        sessionConfig.addAnnotatedClass(TransportagentGetListBapi.class);
        sessionConfig.addAnnotatedClass(MaterialGetListBapi.class);
        sessionConfig.addAnnotatedClass(GoodsMvtPOSTOCreatePGIBapi.class);
        sessionConfig.addAnnotatedClass(VendorValiationCheckBapi.class);
        sessionConfig.addAnnotatedClass(SOGetDetailBapi.class);
        sessionConfig.addAnnotatedClass(PoPostGetListBapi.class);
        sessionConfig.addAnnotatedClass(SyncContractSOGetListBapi.class);
        sessionConfig.addAnnotatedClass(DOPostingPGIBapi.class);
        sessionConfig.addAnnotatedClass(CustomerGetListBapi.class);
        sessionConfig.addAnnotatedClass(SaleOrderGetDetailBapi.class);
        
        AnnotationConfiguration conf = new AnnotationConfiguration(sessionConfig);
        return conf.buildSessionManager();
    }
}
