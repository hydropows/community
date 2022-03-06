/********************************************************************************
 * Purpose: 实现前端界面功能
 * Author: Caijinbang
 * Created Date:2022-1-10
 * Modify Description:
 * 1. 实现码农社区帖子回复评论请求 (Caijinbang 2022-1-10)
 * 2. 新增帖子问题回复不刷新页面登录 (Caijinbang 2022-1-14)
 * 3. 新增展开二级评论方法 (Caijinbang 2022-1-30)
 * 4. 新增点击标签填入标签栏中的方法 (Caijinbang 2022-2-15)
 ** *****************************************************************************/

/*************************************************************************************************
 * Purpose: 实现码农社区帖子回复评论请求
 *************************************************************************************************/
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    comment2target(questionId, 1, content);
}

/*************************************************************************************************
 * Purpose:
 * 1. 判断回复类型
 * 2. 实现post提交回复内容
 * 3. 帖子问题回复不刷新页面登录
 *************************************************************************************************/
function comment2target(targetId, type, content) {
    if (!content) {
        alert("不能回复空内容~~~");
        return;
    }

    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: 'application/json',
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            //回复成功
            if (response.code == 200) {
                window.location.reload();
            } else {
                //用户未登录
                if (response.code == 2003) {
                    var isAccepted = confirm(response.message);
                    if (isAccepted) {
                        $('#myModal').modal({});
                        // window.open("https://github.com/login/oauth/authorize?client_id=3a45192d5234a618a916&redirect_uri=http://localhost:8080/callback&scope=user&state=1")
                        // window.open("https://github.com/login/oauth/authorize?client_id=7f316909bf70d1eaa2b2&redirect_uri=" + document.location.origin + "/callback&scope=user&state=1");
                        // window.localStorage.setItem("closable", true);
                    }
                } else {
                    alert(response.message);
                }
            }
        },
        dataType: "json"
    });
}

/**
 * 提交评论的回复
 */
function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();
    comment2target(commentId, 2, content);
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);

    // 获取一下二级评论的展开状态
    var collapse = e.getAttribute("data-collapse");
    if (collapse) {
        // 折叠二级评论
        comments.removeClass("in");
        e.removeAttribute("data-collapse");
        e.classList.remove("active");
    } else {
        var subCommentContainer = $("#comment-" + id);
        //判断该评论是否为初次加载，若不是则不需要再调用请求获取
        if (subCommentContainer.children().length != 1) {
            // 展开二级评论
            comments.addClass("in");
            // 标记二级评论展开状态
            e.setAttribute("data-collapse", "in");
            e.classList.add("active");
        }
        //初次加载二级评论，需要 post 请求获取数据
        else {
            $.getJSON("/comment/" + id, function (data) {
                /**
                 * $(selector).each(function(index,element))
                 * function(index,element)
                 * index : 选择器的 index 位置
                 * element : 当前的元素 (也可使用 "this" 选择器)
                 */
                $.each(data.data.reverse(), function (index, comment) {
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {
                        "class": "pull-right",
                        // "html": comment.gmtCreate
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments"
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);
                });
                //展开二级评论
                comments.addClass("in");
                // 标记二级评论展开状态
                e.setAttribute("data-collapse", "in");
                e.classList.add("active");
            });
        }
    }
}

/**
 * 点击便签栏展示所有标签
 */
function showSelectTag() {
    $("#select-tag").show();
}

/**
 * 点击标签填入标签栏中
 */
function selectTag(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();

    if (previous) {
        var index = 0;
        var appear = false; //记录value是否已经作为一个独立的标签出现过
        while (true) {
            index = previous.indexOf(value, index); //value字符串在previous中出现的位置
            if (index == -1) break;
            //判断previous中出现的value是否是另一个标签的一部分
            //即value的前一个和后一个字符都是逗号","或者没有字符时，才说明value是一个独立的标签
            if ((index == 0 || previous.charAt(index - 1) == ",")
                && (index + value.length == previous.length || previous.charAt(index + value.length) == ",")
               ) {
                appear = true;
                break;
            }
            index++; //用于搜索下一个出现位置
        }
        if (!appear) {
            //若value没有作为一个独立的标签出现过
            $("#tag").val(previous + ',' + value);
        }
    }
    else {
        $("#tag").val(value);
    }
}