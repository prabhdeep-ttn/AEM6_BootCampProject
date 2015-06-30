$(document).ready(function(){
    var page=$("#page").val();
    if(page == "null"){
        page=1;
    }
    displayRSS(page);
});

function displayRSS(id){
    history.pushState({id: 'changeUrl'}, '', '?page='+id); // Changes Url Without Refreshing it
    var parentPath=$("#parentPath").val();
    var servletUrl=$("#url").val();
    var noOfFeeds=$("#noOfFeeds").val();
    $.ajax({
        type: "GET",
        url: servletUrl,
        data: {
            path:parentPath,
            feeds:noOfFeeds,
            pageNo:id
        },
        success: function(data, status, xhr) {
            var pages=data.pages;
            var total=data.total;
            printLinks(pages);
            
            $(".refresh").on("click", function test(event){
                displayRSS(event.target.id);
                return false;
            });
            
            var rssFeeds="";
            $.each(data.feeds, function(key, value) {
                rssFeeds+="<a href="+value.id+">"+value.title+"</a><br/>";
            });
            $("#RSSFeeds").html(rssFeeds);
        }
    });
    return false;
}

function printLinks(noOfLinks){
    var links="";
    var i;
    for(i=1;i<=noOfLinks;i++){
        links+="&nbsp<a href='' class='refresh' id='"+i+"'>"+i+"</a>&nbsp"
    }
    $("#index").html(links);
}
