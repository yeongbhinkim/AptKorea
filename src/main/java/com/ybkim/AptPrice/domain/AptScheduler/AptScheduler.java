//package com.ybkim.AptPrice.domain.AptScheduler;
//
//import com.ybkim.AptPrice.domain.AptScheduler.svc.AptApiDbSVC;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.w3c.dom.Document;
//import org.w3c.dom.Element;
//import org.w3c.dom.Node;
//import org.w3c.dom.NodeList;
//import org.xml.sax.SAXException;
//
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.BufferedReader;
//import java.io.ByteArrayInputStream;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.net.HttpURLConnection;
//import java.net.URL;
//import java.net.URLEncoder;
//import java.nio.charset.StandardCharsets;
//import java.text.ParseException;
//import java.time.LocalDate;
//import java.time.format.DateTimeFormatter;
//import java.util.ArrayList;
//import java.util.List;
//
//@Configuration
//@EnableScheduling
//public class AptScheduler {
//
//    @Autowired
//    AptApiDbSVC aptApiDbSVC;
//
//    @Scheduled(cron = "0 05 22 * * ?")
//    public void myScheduledMethod() throws IOException, ParseException, ParserConfigurationException, SAXException {
//
//        // 시작 날짜와 끝 날짜 설정
//        LocalDate startDate = LocalDate.of(2023, 11, 1); // 시작 날짜
//        LocalDate endDate = LocalDate.of(2024, 6, 1);   // 끝 날짜
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMM");
//
//        // 시작 날짜부터 끝 날짜까지 월 단위로 반복
//        for (LocalDate date = startDate; date.isBefore(endDate.plusMonths(1)); date = date.plusMonths(1)) {
//            String formattedDate = date.format(formatter); // 현재 날짜를 형식에 맞게 변환
//            System.out.println("formattedDate = " + formattedDate);
//
//            // 지역 코드 목록 가져오기
//            List<CountyCode> apiRegionCounty = aptApiDbSVC.apiRegionCounty();
//
//            // 각 지역 코드에 대해 데이터 처리
//            for (int i = 0; i < apiRegionCounty.size(); i++) {
//                CountyCode county = apiRegionCounty.get(i); // 현재 지역 코드
//                System.out.println("county.getCounty_code() = " + county.getCounty_code());
//
//                // API URL 생성
//                StringBuilder urlBuilder = new StringBuilder("http://openapi.molit.go.kr/OpenAPI_ToolInstallPackage/service/rest/RTMSOBJSvc/getRTMSDataSvcAptTradeDev");
//                urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=GIzL%2BzqBUKfyHh3o%2F38ufLPiq5P%2FReUOx6NCLFEcP7rqQei%2FPIM3QbUw3rLU1gYOQfdeIREA1cLZF7lRIP5zTg%3D%3D"); // 서비스 키
//                urlBuilder.append("&" + URLEncoder.encode("LAWD_CD", "UTF-8") + "=" + URLEncoder.encode(county.getCounty_code(), "UTF-8")); // 지역 코드
//                urlBuilder.append("&" + URLEncoder.encode("DEAL_YMD", "UTF-8") + "=" + URLEncoder.encode(formattedDate, "UTF-8")); // 거래 연월
//
//                // HTTP 연결 설정 및 요청
//                URL url = new URL(urlBuilder.toString());
//                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//                conn.setRequestMethod("GET");
//                conn.setRequestProperty("Content-type", "application/json");
//                System.out.println("Response code: " + conn.getResponseCode());
//                BufferedReader rd;
//
//                // 응답 코드에 따라 입력 스트림 설정
//                if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//                    rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//                } else {
//                    rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//                }
//
//                // 응답 데이터 읽기
//                StringBuilder sb = new StringBuilder();
//                String line;
//                while ((line = rd.readLine()) != null) {
//                    sb.append(line);
//                }
//                rd.close();
//                conn.disconnect();
//
//                String xmlData = sb.toString(); // XML 데이터로 변환
//
//                // DocumentBuilderFactory 인스턴스 생성
//                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//                DocumentBuilder builder = factory.newDocumentBuilder();
//
//                // String 형태의 XML 데이터를 InputStream으로 변환
//                ByteArrayInputStream input = new ByteArrayInputStream(xmlData.getBytes(StandardCharsets.UTF_8));
//
//                // XML 데이터 파싱
//                Document doc = builder.parse(input);
//                doc.getDocumentElement().normalize();
//
//                // "item" 태그로 요소들을 가져옴
//                NodeList nList = doc.getElementsByTagName("item");
//                List<AptApiDb> aptApiDbs = new ArrayList<>();
//
//                for (int temp = 0; temp < nList.getLength(); temp++) {
//                    Node node = nList.item(temp);
//
//                    if (node.getNodeType() == Node.ELEMENT_NODE) {
//                        Element element = (Element) node;
//
//                        // 원하는 태그의 데이터 추출
//                        String 거래유형 = element.getElementsByTagName("거래유형").item(0).getTextContent();
//                        String 거래금액 = element.getElementsByTagName("거래금액").item(0).getTextContent();
//                        String 건축년도 = element.getElementsByTagName("건축년도").item(0).getTextContent();
//                        String 년 = element.getElementsByTagName("년").item(0).getTextContent();
//                        String 도로명 = element.getElementsByTagName("도로명").item(0).getTextContent();
//                        String 법정동 = element.getElementsByTagName("법정동").item(0).getTextContent();
//                        String 법정동본번코드 = element.getElementsByTagName("법정동본번코드").item(0).getTextContent();
//                        String 법정동부번코드 = element.getElementsByTagName("법정동부번코드").item(0).getTextContent();
//                        String 법정동시군구코드 = element.getElementsByTagName("법정동시군구코드").item(0).getTextContent();
//                        String 아파트 = element.getElementsByTagName("아파트").item(0).getTextContent();
//                        String 월 = element.getElementsByTagName("월").item(0).getTextContent();
//                        String 일 = element.getElementsByTagName("일").item(0).getTextContent();
//                        String 전용면적 = element.getElementsByTagName("전용면적").item(0).getTextContent();
//                        String 중개사소재지 = element.getElementsByTagName("중개사소재지").item(0).getTextContent();
//                        String 지번 = element.getElementsByTagName("지번").item(0).getTextContent();
//                        String 층 = element.getElementsByTagName("층").item(0).getTextContent();
//                        String 해제사유발생일 = element.getElementsByTagName("해제사유발생일").item(0).getTextContent();
//
//                        String city = aptApiDbSVC.selectCity(법정동시군구코드).getSCHEDULER_CITY_CODE() + 법정동;
//
//                        if (월.length() == 1) {
//                            월 = "0" + 월;
//                        }
//
//                        String contract_date = 년 + 월;
//
//                        AptApiDb aptApiDb = new AptApiDb();
//                        aptApiDb.setCity(city);
//                        aptApiDb.setStreet(지번);
//                        aptApiDb.setBon_bun(법정동본번코드);
//                        aptApiDb.setBu_bun(법정동부번코드);
//                        aptApiDb.setDan_gi_myeong(아파트);
//                        aptApiDb.setSquare_meter(Float.parseFloat(전용면적));
//                        aptApiDb.setContract_date(contract_date);
//                        aptApiDb.setContract_day(일);
//                        aptApiDb.setAmount(Integer.parseInt(거래금액.replace(",", "").trim()));
//                        aptApiDb.setLayer(Integer.parseInt(층));
//                        aptApiDb.setConstruction_date(건축년도);
//                        aptApiDb.setRoad_name(도로명);
//                        aptApiDb.setReason_cancellation_date(해제사유발생일);
//                        aptApiDb.setTransaction_type(거래유형);
//                        aptApiDb.setLocation_agency(중개사소재지);
//
//                        // FULL_CONTRACT_DATE 설정
//                        String year = contract_date.substring(0, 4);
//                        String month = contract_date.substring(4, 6);
//                        int dayInt = Integer.parseInt(일);
//                        aptApiDb.setFullContractDate(year + "-" + month + "-" + String.format("%02d", dayInt));
//
//                        // 추출된 데이터 출력 또는 처리
//                        System.out.println("aptApiDb: " + aptApiDb);
//
//                        // 추가적인 데이터 처리 (예: 데이터베이스에 삽입)
//                        try {
//                            // aptApiDbSVC.ApiDb(aptApiDb);
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//                System.out.println("======================완료===================");
//            }
//        }
//    }
//}
