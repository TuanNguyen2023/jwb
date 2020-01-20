
CREATE PROCEDURE store_bt_WeightTicket
(  
@id_report nvarchar(100),
@mandt nvarchar(100), 
@plant nvarchar(100), 
@id nvarchar(100), 
@seq_by_day nvarchar(100)
)
AS 
	SELECT 
		WeightTicket.MANDT AS MANDT,
		WeightTicket.ID AS ID,
		WeightTicket.SEQ_BY_DAY AS SEQ_BY_DAY,
		WeightTicket.SEQ_BY_MONTH AS SEQ_BY_MONTH,
		WeightTicket.TEN_TAI_XE AS TEN_TAI_XE,
		WeightTicket.CMND_BL AS CMND_BL,
		WeightTicket.SO_XE AS SO_XE,
		WeightTicket.SO_ROMOOC AS SO_ROMOOC,
		WeightTicket.REG_CATEGORY AS REG_CATEGORY,
		WeightTicket.REG_ITEM_TEXT AS REG_ITEM_TEXT,
		WeightTicket.REG_ITEM_QTY AS REG_ITEM_QTY,
		WeightTicket.CREATE_DATE AS CREATE_DATE,
		WeightTicket.CREATE_TIME AS CREATE_TIME,
		WeightTicket.CREATOR AS CREATOR,
		WeightTicket.WPlant AS WPlant,
		(
			SELECT Name1 FROM SAPSetting
			WHERE SAPSetting.MANDT = WeightTicket.MANDT AND SAPSetting.WPlant = WeightTicket.WPlant
		) AS PName1,
		(
			SELECT Name2 FROM SAPSetting
			WHERE SAPSetting.MANDT = WeightTicket.MANDT AND SAPSetting.WPlant = WeightTicket.WPlant
		) AS PName2,
		(
			SELECT Name_RPT FROM rpt_address
			WHERE rpt_address.rpt_id = @id_report
		) AS PName_RPT,
		(
			SELECT ADDRESS FROM rpt_address 
			WHERE rpt_address.rpt_id = @id_report
		) AS PAddress,
	   (
			SELECT Phone FROM rpt_address
			WHERE rpt_address.rpt_id = @id_report
		) AS PPhone,
		(
			SELECT FAX FROM rpt_address
			WHERE rpt_address.rpt_id = @id_report
		) AS PFAX,
		WeightTicket.LGORT AS LGORT,
		(
			SELECT LGOBE FROM SLoc WHERE SLoc.MANDT = WeightTicket.MANDT
			AND SLoc.WPlant = WeightTicket.WPlant AND SLoc.LGORT = WeightTicket.LGORT
		) AS SLocName,
		WeightTicket.CHARG AS CHARG,
		WeightTicket.DELIV_NUMB AS DELIV_NUMB,
		OutbDel.LIFNR,
		(
			SELECT NAME1 FROM Vendor
			WHERE Vendor.MANDT = OutbDel.MANDT
			AND Vendor.LIFNR = OutbDel.LIFNR
		) AS DOSPName1,
		(
			SELECT NAME2
		   FROM Vendor
		   WHERE Vendor.MANDT = OutbDel.MANDT
			 AND Vendor.LIFNR = OutbDel.LIFNR
		) AS DOSPName2,
		WeightTicket.KUNNR,
		(
			SELECT NAME1 FROM Customer
			WHERE Customer.MANDT = WeightTicket.MANDT
			AND Customer.KUNNR = WeightTicket.KUNNR
		) AS SHPName1,
		(
			SELECT NAME2 FROM Customer
		   WHERE Customer.MANDT = WeightTicket.MANDT
			 AND Customer.KUNNR = WeightTicket.KUNNR
		) AS SHPName2,
		OutbDel.KUNAG,
		(
			SELECT NAME1 FROM Customer
			WHERE Customer.MANDT = OutbDel.MANDT AND Customer.KUNNR = OutbDel.KUNAG
		) AS SOPName1,
		(
		SELECT NAME2 FROM Customer
		WHERE Customer.MANDT = OutbDel.MANDT AND Customer.KUNNR = OutbDel.KUNAG
		) AS SOPName2,
		OutbDel.RECV_PLANT,
		(
			SELECT Name1 FROM SAPSetting WHERE SAPSetting.MANDT = WeightTicket.MANDT
			AND SAPSetting.WPlant = OutbDel.RECV_PLANT
		) AS RPName1,
		(
			SELECT Name2 FROM SAPSetting
			WHERE SAPSetting.MANDT = WeightTicket.MANDT AND SAPSetting.WPlant = OutbDel.RECV_PLANT
		) AS RPName2,
		OutbDel.WERKS AS ISSU_PLANT,
	   (
			SELECT Name1 FROM SAPSetting WHERE SAPSetting.MANDT = WeightTicket.MANDT
			AND SAPSetting.WPlant = OutbDel.WERKS
		) AS IPName,
		WeightTicket.EBELN AS EBELN,
		PurOrder.PLANT AS PORPlant,
		(
			SELECT Name1 FROM SAPSetting WHERE SAPSetting.MANDT = WeightTicket.MANDT
			AND SAPSetting.WPlant = PurOrder.PLANT
		) AS PORPName1,
		(
			SELECT Name2 FROM SAPSetting 
			WHERE SAPSetting.MANDT = WeightTicket.MANDT AND SAPSetting.WPlant = PurOrder.PLANT
		) AS PORPName2,
		(
			SELECT Name_RPT FROM SAPSetting
			WHERE SAPSetting.MANDT = WeightTicket.MANDT
			AND SAPSetting.WPlant = PurOrder.PLANT
		) AS PORPName_RPT,
		PurOrder.SUPPL_VEND,
		(
			SELECT NAME1 FROM Vendor
			WHERE Vendor.MANDT = PurOrder.MANDT
			AND Vendor.LIFNR = PurOrder.SUPPL_VEND) AS PURSVName1,
		(
			SELECT NAME2 FROM Vendor
			WHERE Vendor.MANDT = PurOrder.MANDT
			AND Vendor.LIFNR = PurOrder.SUPPL_VEND
		) AS PURSVName2,
		PurOrder.Customer,
		(
			SELECT NAME1 FROM Customer
			WHERE Customer.MANDT = PurOrder.MANDT
			AND Customer.KUNNR = PurOrder.Customer
		) AS CName1,
		(
			SELECT NAME2 FROM Customer
			WHERE Customer.MANDT = PurOrder.MANDT
			AND Customer.KUNNR = PurOrder.Customer
		) AS CName2,
		PurOrder.SUPPL_PLNT,
		(
			SELECT NAME1 FROM Vendor
			WHERE Vendor.MANDT = PurOrder.MANDT
			AND Vendor.LIFNR = PurOrder.SUPPL_PLNT
		) AS PURSPName1,
		(
			SELECT NAME2 FROM Vendor
			WHERE Vendor.MANDT = PurOrder.MANDT
			AND Vendor.LIFNR = PurOrder.SUPPL_PLNT
		) AS PURSPName2,
		WeightTicket.RECV_LGORT,
		(
			SELECT LGOBE FROM SLoc 
			WHERE SLoc.MANDT = WeightTicket.MANDT
			AND SLoc.WPlant = WeightTicket.WPlant
			AND SLoc.LGORT = WeightTicket.RECV_LGORT
		) AS RecvSLocName,
		WeightTicket.ITEM AS ITEM,
		WeightTicket.MATNR_REF AS MATNR_REF,
		WeightTicket.TEXT AS TEXT,
		WeightTicket.SO_NIEM_XA AS SO_NIEM_XA,
		WeightTicket.F_SCALE/1000 AS F_SCALE,
		WeightTicket.F_TIME AS F_TIME,
		WeightTicket.F_CREATOR AS F_CREATOR,
		WeightTicket.S_SCALE/1000 AS S_SCALE,
		WeightTicket.S_TIME AS S_TIME,
		WeightTicket.S_CREATOR AS S_CREATOR,
		WeightTicket.G_QTY AS G_QTY,
		WeightTicket.UNIT AS UNIT,
		WeightTicket.MAT_DOC AS MAT_DOC,
		WeightTicket.DOC_YEAR AS DOC_YEAR,
		WeightTicket.TRANSFERED_POSTING AS TRANSFERED_POSTING,
		WeightTicket.OFFLINE_MODE AS OFFLINE_MODE,
		WeightTicket.DISSOLVED AS DISSOLVED,
		WeightTicket.POSTED AS POSTED,
		(
			CASE
				WHEN WeightTicket.MOVE_TYPE = '313' THEN 'Gia công ngoài'
				WHEN WeightTicket.MOVE_TYPE = '311' THEN 'C.Kho nội bộ'
				END
		) AS MVT,
		(
			CASE
				WHEN (S_SCALE IS NULL
					  AND REG_CATEGORY = 'I') THEN 'PHIẾU YÊU CẦU NHẬP HÀNG'
				WHEN (S_SCALE IS NOT NULL
					  AND REG_CATEGORY = 'I') THEN 'PHIẾU NHẬP HÀNG'
				WHEN (S_SCALE IS NULL
					  AND REG_CATEGORY = 'O') THEN 'PHIẾU YÊU CẦU XUẤT HÀNG'
				WHEN (S_SCALE IS NOT NULL
					  AND REG_CATEGORY = 'O') THEN 'PHIẾU XUẤT HÀNG'
				END
		) AS TITLE,
		(
			CASE
				WHEN S_SCALE IS NULL THEN 'P.X Sản Xuất'
				WHEN (S_SCALE IS NOT NULL
					  AND REG_CATEGORY = 'I') THEN 'Người giao hàng'
				WHEN (S_SCALE IS NOT NULL AND REG_CATEGORY = 'O') THEN 'Người nhận hàng'
				END
		) AS SIGN3
		FROM WeightTicket WeightTicket
		LEFT OUTER JOIN OutbDel OutbDel ON WeightTicket.MANDT = OutbDel.MANDT
		AND WeightTicket.DELIV_NUMB = OutbDel.DELIV_NUMB
		LEFT OUTER JOIN PurOrder PurOrder ON WeightTicket.MANDT = PurOrder.MANDT
		AND WeightTicket.EBELN = PurOrder.PO_NUMBER
		WHERE
		WeightTicket.MANDT = @mandt 
		AND 
		WeightTicket.WPlant =  @plant
		AND
		WeightTicket.ID =@id
		AND
		WeightTicket.SEQ_BY_DAY = @seq_by_day;