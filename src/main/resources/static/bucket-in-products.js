
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
    stomp.send("products/{id}/bucket", {}, JSON.stringify({
        'id': $("#id").val(),
        'title': $("#title").val(),
        'price': $("#price").val()
    }));
    console.log('Sending');
}

// рендер сообщения, полученного от сервера
function renderItem(bucketDto) {
console.log('We are in render now');
    var bucket = JSON.parse(bucketDto.body);
    amountId.innerText=bucket.amountProducts;
    sumId.innerText=bucket.sum;
    console.log('my bucket amount products: ' + bucket.amountProducts);
    console.log('my bucket sum: ' + bucket.sum);
}
