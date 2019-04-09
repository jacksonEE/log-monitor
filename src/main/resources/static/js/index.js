/**
 * created by Jackson at 2018/12/2
 **/
let currentFontCls = "c12";
let globalIndex = 1; //全局页数
let maxLen = 150;
let contents = [];
let query = {
    env: '',
    level: '',
    service: '',
    startTime: '',
    endTime: '',
    isPlay: true,
    interval: 10,
    searchType: 'msg'
};

function getUrlParam(name) {
    let reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    let r = window.location.search.substr(1).match(reg);
    return r != null ? unescape(r[2]) : '';
}

let loadData = function () {
    $('#exampleTable')
        .bootstrapTable(
            {
                method: 'get', // 服务器数据的请求方式 get or post
                url: "/log/list", // 服务器数据的加载地址
                striped: false, // 设置为true会有隔行变色效果
                async: true,
                cache: false,
                dataType: "json", // 服务器返回的数据类型
                pagination: true, // 设置为true会在底部显示分页条
                queryParamsType: '',
                pageSize: 30,
                pageList: [30, 40, 50],
                // //设置为limit则会发送符合RESTFull格式的参数
                singleSelect: false, // 设置为true将禁止多选
                toolbar: '#toolbar',
                queryParams: queryParams,
                search: true,
                showColumns: false, // 是否显示内容下拉框（选择显示的列）
                sidePagination: "server", // 设置在哪里进行分页，可选值为"client",
                showRefresh: true,
                responseHandler: function (res) {
                    venderData(res.data);
                    return {
                        total: res.count,
                        rows: []
                    }
                },
                columns: []
            });
};

let loadMore = false;
function venderData(data) {
    if (!loadMore) {
        $("#logDatas").empty();
    }
    data.forEach(item=> {
        let envCls = 'label-default';
        if (item.env == "dev") {
        } else if (item.env == "test") {
            envCls = 'label-danger';
        } else {
            envCls = 'label-success';
        }

        let $tr = $('<tr class="kbnDocTable__row>"</tr>');
        let $t0 = $('<td class="eui-textNoWrap" width="1%"></td>');
        $t0.append( '<ul><li style="margin-top: 10px;"><span style="font-size: 12px;text-align: center;" class="label ' + envCls + '">' + item.env + '</span></li></ul>' );

        let $t1 = $('<td class="eui-textNoWrap" width="1%">' +
            '<ul>' +
            '<li><span style="font-size: 12px">' + item.service + '</span></li>' +
            '<li><span style="font-size: 12px">' + item.time + '</span></li>' +
            '</ul>' +
            '</td>');
        let $t2 = $('<td class="eui-textBreakAll eui-textBreakWord"></td>');
        let $div = $('<div></div>');
        let $span = $('<span name="content" class=' + currentFontCls + ' ></span>');
        let $dl = $('<dl class="source log-target"></dl>');


        let msgColor = '#0d3cde';
        let levelColor = '#10bf38';
        if (item.level == "WARN") {
            levelColor = '#a69107';
        } else if (item.level == "ERROR") {
            levelColor = '#990326';
            msgColor = '#bd0233';
        } else if (item.level == "DEBUG") {
            levelColor = '#103ada';
        }

        //$dl.append('<dd><span class="label ' + envCls + '">' + item.env + '</span></dd>')
        $dl.append('<dt>level:</dt><dd><span style="color: ' + levelColor + '; ">' + item.level + '</span></dd>')
            .append('<dt>pid:</dt><dd><span>' + item.pid + '</span></dd>')
            .append('<dt>thread:</dt><dd><span>' + item.thread + '</span></dd>')
            .append('<dt>class:</dt><dd><span style="color: #14a2c8">' + item.clazz + '</span></dd>')
            .append('<dt>traceId:</dt><dd><span style="color: #7a12a2">' + item.traceId + '</span></dd>')
            .append('<p><dt>message:</dt><dd>' + //white-space: pre-wrap;
                '<span class="neirong_nr" style="color: ' + msgColor + '">' + item.msg + '</span>' +
                '<span class="zk_kuanwen" style="font-size: 12px" sh="1" onclick="showOrHide(this)"></span><span hidden>'+ item.msg +'</span></dd>' +
                '</p>');
        $tr.append($t0);
        $tr.append($t1);
        $span.append($dl);
        $div.append($span);
        $t2.append($div);
        $tr.append($t2);
        $("#logDatas").append($tr);
    });
    globalIndex = globalIndex + 1;
    $("body").mLoading("hide");
    contents = $(".neirong_nr");
    //hide();
}

/*let hide = function (){
    $.each($(".neirong_nr"),function(){
        if($(this).text().length > maxLen){
            $(this).text($(this).text().substring(0,maxLen));
            $(this).html($(this).html()+'...');
            $(this).parent().find('.zk_kuanwen').html("&nbsp;&nbsp;查看更多");
        } else {
        }
    });
};

let showOrHide = function (obj) {
    let sh = $(obj).attr("sh");
    if (sh === "1") {
        $(obj).parent().find('.neirong_nr').html($(obj).next().text());
        $(obj).html("&nbsp;&nbsp;收起");
        $(obj).attr("sh","2");
    } else {
        $(obj).parent().find('.neirong_nr').text($(obj).next().text().substring(0,maxLen) + "...");
        $(obj).html(" &nbsp;&nbsp;查看更多");
        $(obj).attr("sh","1");
    }
};*/


function queryParams(params) {

    let index = params.pageNumber;
    if (loadMore) {
        index = globalIndex;
    }

    return {
        project: getUrlParam('project') || '',
        index: index,
        size: params.pageSize,
        env: query.env,
        service: query.service,
        level: query.level,
        message: params.searchText,
        startTime: query.startTime,
        endTime: query.endTime,
        searchType: query.searchType
    };
}


let job = function () {
    if (query.isPlay) {
        if (query.interval === 0) {
            refresh();
        } else {
            query.interval--;
        }
    } else {
        console.log("pause")
    }
};

let s1;

function switchPlayOrPause(obj) {
    let span = $(obj).children("span");
    let clsName = span.attr('class');
    if (clsName === 'glyphicon glyphicon-play') {
        span.removeClass();
        span.addClass('glyphicon glyphicon-pause');
        query.isPlay = true;
        s1 = setInterval(job, 1000);
    } else {
        span.removeClass();
        span.addClass('glyphicon glyphicon-play');
        query.isPlay = false;
        clearInterval(s1);
    }
}

function refresh() {
    $('#exampleTable').bootstrapTable('refresh');
    resetInterval();
    globalIndex = 1;
}

function resetInterval() {
    query.interval = 10;
    if (!query.isPlay) {
        $("#playSpan").removeClass().addClass('glyphicon glyphicon-play');
        clearInterval(s1);
    }
}

function valChange(obj, param) {
    loadMore = false;
    switch (param) {
        case 'env' :
            query.env = $(obj).val();
            break;
        case 'service':
            //query.service = $(obj).attr("v").trim();
            query.service = $(obj).val();
            break;
        case 'level':
            query.level = $(obj).val();
            break;
        case 'startTime':
            let ed = $("#endDate").val();
            let sd = $(obj).val();
            if (ed.length > 0) {
                let d1 = new Date(sd.replace(/\-/g, "\/"));
                let d2 = new Date(ed.replace(/\-/g, "\/"));
                if (d1 >= d2) {
                    $(obj).val('');
                    return
                }
            }
            query.startTime = new Date($(obj).val()).getTime();
            break;
        case 'endTime' :
            let startDate = $("#startDate").val();
            let endDate = $(obj).val();
            if (startDate.length > 0) {
                let d1 = new Date(startDate.replace(/\-/g, "\/"));
                let d2 = new Date(endDate.replace(/\-/g, "\/"));
                if (d1 >= d2) {
                    $(obj).val('');
                    return
                }
            }
            query.endTime = new Date($(obj).val()).getTime();
            break;
        case 'searchType':
            query.searchType = $(obj).val();
            break;
        case 'reset':
        default:
            query.env = '';
            $("#envSelect").val('');
            query.service = '';
            $("#serviceSelect").val('');
            query.level = '';
            $("#levelSelect").val('');
            query.startTime = '';
            $("#startDate").val('');
            query.endTime = '';
            $("#endDate").val('');
            query.isPlay = false;
            /*let opts = $("#exampleTable").bootstrapTable('getOptions');
            opts.searchText = '';*/
            $("#exampleTable").bootstrapTable('resetSearch', '');
            break;
    }
    refresh();
}


//服务相关
function addService(obj) {
    let project = getUrlParam('project') || '';
    if (project == '' || project == 'jackson') {
        return;
    }
    $(obj).attr('disabled', 'disabled');
    let addDiv = $('<div class="list-group-item col-sm-12" id="addServiceDiv">' +
        '<div class="col-sm-7">' +
        '<input type="text" placeholder="请输出项目名..." class="form-control form-inline" id="serviceName">' +
        '</div>' +
        '<div class="col-sm-2">' +
        '<button class="btn btn-sm" id="confirmBtn" onclick="createService()">' +
        '确定' +
        '</button>' +
        '</div>' +
        '<div class="col-sm-1">' +
        '<a class="btn btn-sm" id="cancelBtn" onclick="cancelAddService(this)">' +
        '取消' +
        '</a>' +
        '</div>' +
        '</div>');

    $("#serviceList").prepend(addDiv);
}

function cancelAddService(obj) {
    $(obj).parent().parent().remove();
    $("#addServiceBtn").removeAttr('disabled');
}


function createService() {
    let serviceName = $("#serviceName").val();
    if (serviceName === '' || serviceName.trim().length === 0) {
        return;
    }
    $.ajax({
        url: '/service/create/' + serviceName,
        type: 'post',
        dataType: 'json',
        success: res => {
            let id = res.id;
            let dataDiv = $('<div class="list-group-item col-sm-12">' +
                '<div class="col-sm-10">' +
                '<a class="bd-toc-link" href="javascript:void(0)" onclick="valChange(this,\'service\')"' +
                'v="' + serviceName + '">' + serviceName + '</a>' +
                '</div>' +
                '<div>' +
                '<button class="btn btn-sm btn-default" onclick="deleteService(this,\'' + id + '\')">' +
                '<span style="color: red" class="glyphicon glyphicon-minus"></span>' +
                '</button>' +
                '</div>' +
                '</div>');
            cancelAddService($("#cancelBtn"));
            $("#serviceList").prepend(dataDiv);
        },
        error: err => {
            console.log(err);
        }
    });
}

let deleteService = function (obj, id) {
    $.ajax({
        url: '/service/delete/' + id,
        type: 'post',
        dataType: 'json',
        success: res => {
            $(obj).parent().parent().remove();
        }
    });
};

let loadServices = function () {
    $.ajax({
        url: '/service/list',
        dataType: 'json',
        success: res => {
            $.each(res, (index, item) => {
                let dataDiv = $('<div class="list-group-item col-sm-12">' +
                    '<div class="col-sm-10">' +
                    '<a class="bd-toc-link" href="javascript:void(0)" onclick="valChange(this,\'service\')"' +
                    'v="' + item.name + '">' + item.name + '</a>' +
                    '</div>' +
                    '<div>' +
                    '<button class="btn btn-sm btn-default" onclick="deleteService(this,\'' + item.id + '\')">' +
                    '<span style="color: red" class="glyphicon glyphicon-minus"></span>' +
                    '</button>' +
                    '</div>' +
                    '</div>');
                $("#serviceList").append(dataDiv);
            });
        },
        error: err => {
            alert('获取项目列表失败');
            console.log('请求获取应用列表出现异常:' + err);
        }
    });
};


//获取滚动条当前的位置
function getScrollTop() {
    let scrollTop = 0;
    if (document.documentElement && document.documentElement.scrollTop) {
        scrollTop = document.documentElement.scrollTop;
    } else if (document.body) {
        scrollTop = document.body.scrollTop;
    }
    return scrollTop;
}

//获取当前可视范围的高度
function getClientHeight() {
    let clientHeight = 0;
    if (document.body.clientHeight && document.documentElement.clientHeight) {
        clientHeight = Math.min(document.body.clientHeight, document.documentElement.clientHeight);
    } else {
        clientHeight = Math.max(document.body.clientHeight, document.documentElement.clientHeight);
    }
    return clientHeight;
}

//获取文档完整的高度
function getScrollHeight() {
    return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight);
}


//滚动事件触发
window.onscroll = function () {
    if (getScrollTop() + getClientHeight() == getScrollHeight()) {
        loadMore = true;
        $("body").mLoading();
        $('#exampleTable').bootstrapTable('refresh');
    }
};

function fontSizeChange(obj) {
    $("span[name='content']").removeClass().addClass($(obj).val());
    currentFontCls = $(obj).val();
}
