// ボタン、モダル、モダルの閉じるボタン、オーバーレイを変数に格納
const btnModal = document.getElementById('');
const modal = document.getElementById('modal');
const btnClose = document.getElementById('close');
const overlay = document.getElementById('overlay');

// ボタンをクリックしたら、モダルとオーバーレイに.activeを付ける
btnModal.addEventListener('click', function(ev){
    // aタグのデフォルトの機能を停止する
    ev.preventDefault();
    // モーダルとオーバーレイにactiveクラスを付与する
    modal.classList.add('active');
    overlay.classList.add('active');
});

// モダルの閉じるボタンをクリックしたら、モダルとオーバーレイのactiveクラスを外す
btnClose.addEventListener('click', function(){
    modal.classList.remove('active');
    overlay.classList.remove('active');
});

// オーバーレイをクリックしたら、モダルとオーバーレイのactiveクラスを外す
overlay.addEventListener('click', function() {
    modal.classList.remove('active');
    overlay.classList.remove('active');
});

function check() {
    const formUserDelete = new
    fetch()
}
