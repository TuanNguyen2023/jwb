USE [jweighbridge]
GO
/****** Object:  StoredProcedure [dbo].[store_bt_regwt]    Script Date: 1/11/2020 2:38:12 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [dbo].[store_bt_regwt]
@client nvarchar(30),
@plant nvarchar(30),
@id nvarchar(30),
@dayseq int ,
@idreport nvarchar(30)
AS
SELECT
	WeightTicket.MANDT AS MANDT,
	WeightTicket.WPlant AS WPlant,
	WeightTicket.ID AS ID,
	WeightTicket.SEQ_BY_DAY AS SEQ_BY_DAY,
	WeightTicket.SEQ_BY_MONTH AS SEQ_BY_MONTH,
	WeightTicket.TEN_TAI_XE AS TEN_TAI_XE,
	WeightTicket.CMND_BL AS CMND_BL,
	WeightTicket.SO_XE AS SO_XE,
	WeightTicket.SO_ROMOOC AS SO_ROMOOC,
	WeightTicket.REG_CATEGORY AS REG_CATEGORY,
	WeightTicket.REG_ITEM_TEXT AS REG_ITEM_TEXT,
	WeightTicket.CREATE_DATE AS CREATE_DATE,
	WeightTicket.CREATOR AS CREATOR,
	users.FULL_NAME AS FULL_NAME,
	WeightTicket.DELIV_NUMB AS DELIV_NUMB,
	WeightTicket.DISSOLVED AS DISSOLVED,
	WeightTicket.POSTED AS POSTED,
	WeightTicket.REG_ITEM_QTY AS REG_ITEM_QTY,
	WeightTicket.CREATE_TIME AS CREATE_TIME,
	(
		SELECT Name_RPT from rpt_address where rpt_address.rpt_id = @idreport
	) AS PName_RPT,
	(
		SELECT ADDRESS from rpt_address where rpt_address.rpt_id = @idreport
	) AS PAddress,
	(
		SELECT Phone from rpt_address where rpt_address.rpt_id = @idreport
	) AS PPhone,
	(
		SELECT FAX from rpt_address where rpt_address.rpt_id = @idreport
	) AS PFAX
	FROM Users users
	INNER JOIN WeightTicket WeightTicket ON users.MANDT = WeightTicket.MANDT
	AND users.ID = WeightTicket.CREATOR
	AND users.WPlant = WeightTicket.WPlant
	WHERE
	WeightTicket.MANDT = @client
	AND WeightTicket.WPlant = @plant
	AND WeightTicket.ID = @id
	AND WeightTicket.SEQ_BY_DAY = @dayseq;
