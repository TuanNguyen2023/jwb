Create procedure store_bq_reg_wt
@client nvarchar(100), 
@wplant nvarchar(100),
@id nvarchar(100),
@seq_by_day nvarchar(100),
@id_report nvarchar(100)
AS 
SELECT
	weightTicket.MANDT  AS MANDT,
	weightTicket.WPlant  AS WPlant,
	weightTicket.ID  AS ID,
	weightTicket.SEQ_BY_DAY  AS SEQ_BY_DAY,
	weightTicket.SEQ_BY_MONTH  AS SEQ_BY_MONTH,
	weightTicket.TEN_TAI_XE  AS TEN_TAI_XE,
	weightTicket.CMND_BL  AS CMND_BL,
	weightTicket.SO_XE  AS SO_XE,
	weightTicket.SO_ROMOOC  AS SO_ROMOOC,
	weightTicket.REG_CATEGORY  AS REG_CATEGORY,
	weightTicket.REG_ITEM_TEXT  AS REG_ITEM_TEXT,
	weightTicket.CREATE_DATE  AS CREATE_DATE,
	weightTicket.CREATOR  AS CREATOR,
	userLocal.FULL_NAME  AS FULL_NAME,
	weightTicket.DELIV_NUMB  AS DELIV_NUMB,
	weightTicket.DISSOLVED  AS DISSOLVED,
	weightTicket.POSTED  AS POSTED,
	weightTicket.REG_ITEM_QTY  AS REG_ITEM_QTY,
	weightTicket.CREATE_TIME  AS CREATE_TIME,
	(SELECT Name_RPT from rpt_address where rpt_address.rpt_id  = @id_report) AS PName_RPT,
	(SELECT ADDRESS from rpt_address where rpt_address.rpt_id  = @id_report) AS PAddress,
	(SELECT Phone from rpt_address where rpt_address.rpt_id  =  @id_report) AS PPhone,
	(SELECT FAX from rpt_address where rpt_address.rpt_id  = @id_report) AS PFAX
FROM
	User_local userLocal
	INNER JOIN  WeightTicket weightTicket ON userLocal.MANDT = weightTicket.MANDT 
	AND userLocal.ID  = weightTicket.CREATOR 
	AND userLocal.WPlant  = weightTicket.WPlant 
WHERE
	weightTicket.MANDT  = @client 
	AND
	weightTicket.WPlant  = @wplant
	AND
	weightTicket.ID  = @id
	AND 
	weightTicket.SEQ_BY_DAY  = @seq_by_day;