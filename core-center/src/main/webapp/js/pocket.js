function getWallet(){
    var coin;
    jQuery.ajax({
        type: "post",
        async: false,
        url: "json?type=wallet",
        cache: false,
        success: function (json) {
            //返回的数据用data.d获取内容
            //alert("success");
            coin = json.wallet;
        },
        error: function (err) {
            coin = "0";
        }
    });
    return coin;
}

function hideInvalidOption(coinQty){
    $("#coinQty").find("option").each(function () {
        if ($(this).val() >coinQty) {
            $(this).remove();
        }
    });
}

function bonus(){
    return $("#coinQty").val();
}
