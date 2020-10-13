# Lampyridae
![반딧불이_메인](https://user-images.githubusercontent.com/52819382/95893177-8ddfc100-0dc2-11eb-8a11-ebac129b8fbe.png)

<br>
<br>

### [프로젝트 개요]
> NFC스티커+가로등 주소를 이용한 정확하고 신속한 신고 어플리케이션


### [프로젝트 구성]
  
+ 관리자용 어플리케이션

+ 사용자용 어플리케이션

+ 수신용 서버(담당)
<br>
<br>

## 기술스택
> Android Studio, Oracle, Java

<br>
<br>

## 주요특징
> 관리자용 어플리케이션 -> NFC스티커에 가로등에 대한 위치 정보를 입력(주입)한다

> 사용자용 어플리케이션 -> NFC스티커에 접촉시 자동적으로 신고를 한다. + 특정키 입력시 현재 GPS를 기반으로 자동으로 주변 파출소에 신고가 가능하다

> 서버
- JAVA 언어로 이클립스를 사용하여 구축
- Oracle로 DB서버 구축
- Socket을 활용한 데이터 통신
- Multi Thread를 사용한 다중접속 기능 구현
- 공공데이터api를 활용하여 얻은 XML데이터를 파싱하여 JSON형식으로 변환
