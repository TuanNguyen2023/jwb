CREATE PROCEDURE [dbo].[store_bt_regwthp]
@reportid nvarchar(100), 
@client nvarchar(100),
@plant nvarchar(100),
@id nvarchar(100), 
@seqbyday nvarchar(100) 
AS 
SELECT
		weightTicket.MANDT AS MANDT,
		weightTicket.WPlant AS WPlant,
		weightTicket.ID AS ID,
		weightTicket.SEQ_BY_DAY AS SEQ_BY_DAY,
		weightTicket.SEQ_BY_MONTH AS SEQ_BY_MONTH,
		weightTicket.TEN_TAI_XE AS TEN_TAI_XE,
		weightTicket.CMND_BL AS CMND_BL,
		weightTicket.SO_XE AS SO_XE,
		weightTicket.SO_ROMOOC AS SO_ROMOOC,
		weightTicket.REG_CATEGORY AS REG_CATEGORY,
		weightTicket.REG_ITEM_TEXT AS REG_ITEM_TEXT,
		weightTicket.CREATE_DATE AS CREATE_DATE,
		weightTicket.CREATOR AS CREATOR,
		users.FULL_NAME AS FULL_NAME,
		weightTicket.DELIV_NUMB AS DELIV_NUMB,
		weightTicket.DISSOLVED AS DISSOLVED,
		weightTicket.POSTED AS POSTED,
		weightTicket.REG_ITEM_QTY AS REG_ITEM_QTY,
		weightTicket.CREATE_TIME AS CREATE_TIME,
		(SELECT Name_RPT from rpt_address where rpt_address.rpt_id = @reportid) AS PName_RPT,
		(SELECT ADDRESS from rpt_address where rpt_address.rpt_id = @reportid) AS PAddress,
		(SELECT Phone from rpt_address where rpt_address.rpt_id = @reportid)  AS PPhone,
		(SELECT FAX from rpt_address where rpt_address.rpt_id = @reportid ) AS PFAX,    
		vcustomer.Customer_name,
		outb_details_v2.BZTXT as saledistrict
	FROM
		WeightTicket weightTicket 
		INNER JOIN  Users users ON users.MANDT = weightTicket.MANDT AND users.ID = weightTicket.CREATOR AND users.WPlant = weightTicket.WPlant
		LEFT JOIN vcustomer vcustomer  on weightTicket.KUNNR = vcustomer.customer_id
		LEFT JOIN outb_details_v2 outb_details_v2 on CONCAT(weightTicket.ID,weightTicket.SEQ_BY_DAY) = outb_details_v2.WT_ID
	WHERE
			 weightTicket.MANDT = @client
			 AND weightTicket.WPlant =@plant
			 AND weightTicket.ID = @id
			 AND weightTicket.SEQ_BY_DAY = @seqbyday;

