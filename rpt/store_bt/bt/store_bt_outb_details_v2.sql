Create procedure  store_bt_outb_details_v2
@mandt nvarchar(100),
@deliver_number nvarchar(100)
AS 
SELECT
				outb_details_v2.MANDT AS outb_details_MANDT,     
				outb_details_v2.DELIV_NUMB AS outb_details_DELIV_NUMB,
				outb_details_v2.DELIV_ITEM AS outb_details_DELIV_ITEM,
				outb_details_v2.MATNR AS outb_details_MATNR,
				outb_details_v2.ARKTX AS outb_details_ARKTX,
				outb_details_v2.LFIMG AS outb_details_LFIMG,
				outb_details_v2.MEINS AS outb_details_MEINS,
				outb_details_v2.VGBEL AS outb_details_VGBEL,
				outb_details_v2.FREE_ITEM AS outb_details_FREE_ITEM,
				outb_details_v2.GOODS_QTY AS outb_details_GOODS_QTY,
				outb_details_v2.BZIRK AS outb_details_BZIRK,
				outb_details_v2.BZTXT AS outb_details_BZTXT
			FROM outb_details_v2 outb_details_v2
			WHERE 
			outb_details_v2.MANDT = @mandt
			AND 
			outb_details_v2.DELIV_NUMB = @deliver_number