'use strict';
var now = new Date();


//계약일자 오늘날짜
document.getElementById('contractDate').value = new Date(now.setMonth(now.getMonth() - 1)).toISOString().substring(0, 10);
document.getElementById('contractDateTo').value = new Date().toISOString().substring(0, 10);

// 주소구분 라디오 이벤트
// const $gubunR1 = document.getElementById('gubunR1');
// $gubunR1.addEventListener('click', fn_select_check)
const $searchDongCd = document.getElementById("searchDongCd");
// const $searchChosung = document.getElementById("searchChosung");
const $searchLoad = document.getElementById("searchLoad");
// $searchDongCd.style.cssText = "display: block; ";
// $searchChosung.style.cssText = "display: none; ";
// $searchLoad.style.cssText = "display: none; ";

function fn_select_check(e) {
    if (e == 1) {
        $searchDongCd.style.cssText = "display: block; ";
        // $searchChosung.style.cssText = "display: none; ";
        // $searchLoad.style.cssText = "display: none; ";
    } else if (e == 2) {
        $searchDongCd.style.cssText = "display: none; ";
        // $searchChosung.style.cssText = "display: block; ";
        // $searchLoad.style.cssText = "display: block; ";
    }
}

//주소 셀렉트 박스 이벤트
const $searchSidoCd = document.getElementById('searchSidoCd');
$searchSidoCd.addEventListener('click', sidoCd)
const $searchGugunCd = document.getElementById("searchGugunCd");
$searchGugunCd.addEventListener('click', gugunCd)

//all을 갑을 줘서 null 값이 나오게 유도? 빈값으로 어떻게 주지?

//시도
function sidoCd(e) {
    if (e != '[object PointerEvent]') {
        const url = '/regionCounty/' + e;
        fn_sgg_search(e, url, cbSidoCd);
    }
    // console.log(e)
}

//시군구
function gugunCd(e) {
    if (e != '[object PointerEvent]') {
        const url = '/regionDistricts/' + e;
        fn_sgg_search(e, url, cbGugunCd);
    }
}

//ajax 호출
function fn_sgg_search(param, url, cbSuccess) {
    fetch(url, {
        method: 'GET',
    })
        .then((res) => res.json())
        .then((res) => cbSuccess(res))
        .catch((err) => {
            console.error('Err:', err);
        });
    // console.log('res:', res)

}

//시도 -> 시군구 데이터 셋
function cbSidoCd(res) {
    $searchGugunCd.add = res.data;
    $searchGugunCd.innerHTML = "";
    $searchDongCd.innerHTML = "";

    var opt2 = document.createElement("option");
    opt2.value = " ";
    opt2.innerHTML = "전체";
    $searchGugunCd.appendChild(opt2);

    var opt3 = document.createElement("option");
    opt3.value = " ";
    opt3.innerHTML = "전체";
    $searchDongCd.appendChild(opt3);

    for (var i = 0; i < res.data.length; i++) {
        var opt = document.createElement("option");
        opt.value = res.data[i].county_CODE;
        opt.innerHTML = res.data[i].county_NM;
        $searchGugunCd.appendChild(opt);
    }
}

//시군구 -> 읍면동 데이터 셋
function cbGugunCd(res) {
    $searchDongCd.add = res.data;
    $searchDongCd.innerHTML = "";

    var opt3 = document.createElement("option");
    opt3.value = " ";
    opt3.innerHTML = "전체";
    $searchDongCd.appendChild(opt3);

    for (var i = 0; i < res.data.length; i++) {
        var opt = document.createElement("option");
        opt.value = res.data[i].districts_CODE;
        opt.innerHTML = res.data[i].districts_NM;
        $searchDongCd.appendChild(opt);
    }
}

//검색
const $contractDate = document.getElementById('contractDate');              //시작 계약일자
const $contractDateTo = document.getElementById('contractDateTo');          //종료 계약일자
// const $searchSidoCd = document.getElementById('searchSidoCd');              //시도
// const $searchGugunCd = document.getElementById('searchGugunCd');            //시군구
// const $searchDongCd = document.getElementById('searchDongCd');              //읍면동
const $searchArea = document.getElementById('searchArea');                  //면적
const $searchFromAmount = document.getElementById('searchFromAmount');      //시작 금액
const $searchToAmnount = document.getElementById('searchToAmnount');        //종료 금액
const $searchBtn = document.getElementById('searchBtn');                    //검색버튼
var $searchAreaValue = "";  //시작 면적
var $searchAreaValueTo = "";  //종료 면적
//검색 버튼 클릭시
$searchBtn?.addEventListener('click', search_f);


function search_f(e) {
    //날짜 필수값 체크
    if ($contractDate.value == "" || $contractDateTo.value == "") {
        alert('계약일자를 입력하세요');
        if ($contractDate.value == "") {
            $contractDate.focus();
            $contractDate.select(); //커서이동
        }
        if ($contractDateTo.value == "") {
            $contractDateTo.focus();
            $contractDateTo.select(); //커서이동
        }
        return false;
    }

    //면적 값
    if ($searchArea.value == 1) {
        $searchAreaValue = 0;
        $searchAreaValueTo = 60;
    } else if ($searchArea.value == 2) {
        $searchAreaValue = 60;
        $searchAreaValueTo = 85;
    } else if ($searchArea.value == 3) {
        $searchAreaValue = 85;
        $searchAreaValueTo = 102;
    } else if ($searchArea.value == 4) {
        $searchAreaValue = 102;
        $searchAreaValueTo = 135;
    } else if ($searchArea.value == 5) {
        $searchAreaValue = 135;
        $searchAreaValueTo = 10000;
    } else if ($searchArea.value == 0) {
        $searchAreaValue = 0;
        $searchAreaValueTo = 10000;
    }

    const searchSidoCdText = $searchSidoCd.options[$searchSidoCd.selectedIndex].text == '전체' ? ' ' : $searchSidoCd.options[$searchSidoCd.selectedIndex].text;
    const searchGugunCdText = $searchGugunCd.options[$searchGugunCd.selectedIndex].text == '전체' ? ' ' : $searchGugunCd.options[$searchGugunCd.selectedIndex].text;
    const searchDongCdText = $searchDongCd.options[$searchDongCd.selectedIndex].text == '전체' ? ' ' : $searchDongCd.options[$searchDongCd.selectedIndex].text;
    //
    const url = `/MyHomePrice/list/1/${$contractDate.value}/${$contractDateTo.value}/${searchSidoCdText}/${searchGugunCdText}/${searchDongCdText}/${$searchArea.value}/${$searchAreaValue}/${$searchAreaValueTo}/${$searchFromAmount.value}/${$searchToAmnount.value}`;


    //조건 검색
    // const url = `/MyHomePrice/list/1/${$contractDate.value}/${$contractDateTo.value}/${$searchSidoCd.options[$searchSidoCd.selectedIndex].text}/${$searchGugunCd.options[$searchGugunCd.selectedIndex].text}/${$searchDongCd.options[$searchDongCd.selectedIndex].text}/${$searchArea.value}/${$searchAreaValue}/${$searchAreaValueTo}/${$searchFromAmount.value}/${$searchToAmnount.value}`;
    location.href = url;
}


