
var stomp = null;

// подключаемся к серверу по окончании загрузки страницы
window.onload = function() {
    connect();
};

function connect() {
    var socket = new SockJS('/socket');
    stomp = Stomp.over(socket);
    stomp.connect({}, function (frame) {
        console.log('Connected: ' + frame);
        stomp.subscribe('/topic/products', function (bucketDto) {
            renderItem(bucketDto);
        });
    });
}

// хук на интерфейс
$(function () {

    $( "#add" ).click(function() { sendContent(); });
});

// отправка сообщения на сервер
function sendContent() {
    stomp.send("/app/products/{id}/bucket", {}, JSON.stringify({
        'id': $("#id").val()
        'title': $("#title").val(),
        'price': $("#price").val()
    }));
}

// рендер сообщения, полученного от сервера
function renderItem(bucketDto) {
    var product = JSON.parse(bucketDto.body);
    $("#amountId").setText(bucketDto.getAmountProducts());
    $("#sumtId").setText(bucketDto.getSum());

}
