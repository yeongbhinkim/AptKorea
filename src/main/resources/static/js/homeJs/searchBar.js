'use strict';

// 날짜 설정
const now = new Date();
document.getElementById('contractDate').value = new Date(now.setMonth(now.getMonth() - 1)).toISOString().substring(0, 10);
document.getElementById('contractDateTo').value = new Date().toISOString().substring(0, 10);

// DOM 요소 가져오기
const $searchDongCd = document.getElementById("searchDongCd");
const $searchLoad = document.getElementById("searchLoad");
const $searchSidoCd = document.getElementById('searchSidoCd');
const $searchGugunCd = document.getElementById("searchGugunCd");
const $searchBtn = document.getElementById('searchBtn');
const $contractDate = document.getElementById('contractDate');
const $contractDateTo = document.getElementById('contractDateTo');
const $searchArea = document.getElementById('searchArea');
const $searchFromAmount = document.getElementById('searchFromAmount');
const $searchToAmnount = document.getElementById('searchToAmnount');

let $searchAreaValue = "";  // 시작 면적
let $searchAreaValueTo = "";  // 종료 면적

// 주소 선택 이벤트
$searchSidoCd?.addEventListener('click', sidoCd);
$searchGugunCd?.addEventListener('click', gugunCd);

// 시도 선택 시 처리
function sidoCd(e) {
    if (e !== '[object PointerEvent]') {
        const url = '/regionCounty/' + e;
        fn_sgg_search(e, url, cbSidoCd);
    }
}

// 시군구 선택 시 처리
function gugunCd(e) {
    if (e !== '[object PointerEvent]') {
        const url = '/regionDistricts/' + e;
        fn_sgg_search(e, url, cbGugunCd);
    }
}

// AJAX 요청 함수
function fn_sgg_search(param, url, cbSuccess) {
    fetch(url, {
        method: 'GET',
        mode: 'cors',
        credentials: 'same-origin'
    })
        .then((res) => res.json())
        .then((res) => cbSuccess(res))
        .catch((err) => {
            console.error('Error occurred during data fetch:', err);
            alert('데이터를 불러오는 중 오류가 발생했습니다.');
        });
}

// 시도 데이터 세팅
function cbSidoCd(res) {
    $searchGugunCd.innerHTML = "";
    $searchDongCd.innerHTML = "";

    // '전체' 옵션 추가
    appendOption($searchGugunCd, " ", "전체");
    appendOption($searchDongCd, " ", "전체");

    res.data.forEach(item => {
        appendOption($searchGugunCd, item.county_CODE, item.county_NM);
    });
}

// 시군구 데이터 세팅
function cbGugunCd(res) {
    $searchDongCd.innerHTML = "";

    // '전체' 옵션 추가
    appendOption($searchDongCd, " ", "전체");

    res.data.forEach(item => {
        appendOption($searchDongCd, item.districts_CODE, item.districts_NM);
    });
}

// 옵션을 select 요소에 추가하는 함수
function appendOption(selectElement, value, text) {
    const option = document.createElement("option");
    option.value = value;
    option.innerHTML = text;
    selectElement.appendChild(option);
}

// 검색 버튼 클릭 시 처리
$searchBtn?.addEventListener('click', search_f);

function search_f(e) {
    // 날짜 필수값 체크
    if (!$contractDate.value || !$contractDateTo.value) {
        alert('계약일자를 입력하세요');
        !$contractDate.value ? $contractDate.focus() : $contractDateTo.focus();
        return false;
    }

    // 면적 값 처리
    switch ($searchArea.value) {
        case "1":
            $searchAreaValue = 0; $searchAreaValueTo = 60;
            break;
        case "2":
            $searchAreaValue = 60; $searchAreaValueTo = 85;
            break;
        case "3":
            $searchAreaValue = 85; $searchAreaValueTo = 102;
            break;
        case "4":
            $searchAreaValue = 102; $searchAreaValueTo = 135;
            break;
        case "5":
            $searchAreaValue = 135; $searchAreaValueTo = 10000;
            break;
        default:
            $searchAreaValue = 0; $searchAreaValueTo = 10000;
    }

    const searchSidoCdText = getSelectedText($searchSidoCd, '전체');
    const searchGugunCdText = getSelectedText($searchGugunCd, '전체');
    const searchDongCdText = getSelectedText($searchDongCd, '전체');

    const url = `/MyHomePrice/list/1/${$contractDate.value}/${$contractDateTo.value}/${searchSidoCdText}/${searchGugunCdText}/${searchDongCdText}/${$searchArea.value}/${$searchAreaValue}/${$searchAreaValueTo}/${$searchFromAmount.value}/${$searchToAmnount.value}`;
    location.href = url;
}

// 선택된 옵션의 텍스트를 반환하는 함수
function getSelectedText(selectElement, defaultText) {
    return selectElement?.options[selectElement.selectedIndex].text === defaultText ? ' ' : selectElement.options[selectElement.selectedIndex].text;
}
