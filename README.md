## 🙌 TDD 방법론 스터디 Coffee 프로젝트

## 요구사항
  - 메뉴 조회
    - 커피 정보(메뉴ID, 이름, 가격)을 조회
  - 포인트 충전
    - 사용자 식별값, 충전금액을 입력 받아 포인트를 충전(1원=1P)
  - 주문/결제
    - 사용자 식별값, 메뉴ID를 입력 받아 주문
    - 충전 포인트에서 주문금액 차감
    - 주문 내역을 데이터 수집 플랫폼으로 실시간 전송하는 로직을 추가
      - (Mock API 등을 사용하여 사용자 식별값, 메뉴ID, 결제금액을 전송합니다.)
  - 인기메뉴 목록 조회
    - 최근 7일간 인기있는 메뉴 3개를 조회
    - 메뉴별 주문 횟수가 정확해야 함
  - 제약사항
    1. 다수의 서버에 다수의 인스턴스로 동작하더라도 기능에 문제가 없도록 설계
    2. 각 기능 및 제약사항에 대한 단위테스트를 반드시 작성
    3. 동시성 이슈를 고려하여 구현
    4. 데이터 일관성을 고려하여 구현

## 메뉴 조회 API
> **API명세**
```
  GET /api/coffee/menu/list
  Response
      [
        {"menuId":"1","americano","price":"4000"},
        {"menuId":"2","water","price":"2000"},
        {"menuId":"3","cake","price":"8000"}
      ]
```
> **DB**
```
  - menuid: 메뉴 ID
  - name: 메뉴명
  - price: 가격

CREATE TABLE std_menu(
    menuid VARCHAR(20) NOT NULL,
    name VARCHAR(20) NOT NULL,
    price INT,
CONSTRAINT std_menu_PK PRIMARY KEY(menuid)
);
```
> TEST DATA

  | menuId | name | price |
  | --- | --- | --- |
  | 1 | americano | 4000 |
  | 2 | water | 2000 |
  | 3 | cake | 8000 |
  | 4 | juice | 6000 |
  | 5 | sandwich | 12000 |

> **로직**
```
  - 메뉴ID, 메뉴명, 가격 정보 목록 조회
```
## 포인트 충전 API
> **API명세**
```
  POST /api/coffee/chargePoint
  RequestHeader: Content-Type : application/json
  RequestBody: JSON
      {"userId":"mansol","point":"10000"}
  Response: JSON
      {"status":"ok"}
```
> **DB**
```
  - userId: 유저식별값
  - point: 소유 포인트

CREATE TABLE std_point(
    userid VARCHAR(20) NOT NULL,
    point INT,
CONSTRAINT std_point_PK PRIMARY KEY(userid)
);
```
> TEST DATA

  | userid | point |
  | --- | --- |
  | testUser | 20000 |

> **로직**
```
  - 유저식별값을 입력받아 존재하는 유저인지 체크
  - 유저식별값으로 소유 포인트 확인
  - 존재하는 유저인지 체크 후 소유 포인트에 충전할 포인트 더하여 저장
  - 충전 포인트는 0보다 커야함
```
## 주문/결제 API
> **API명세**
```
  POST /api/coffee/order
  RequestHeader: Content-Type : application/json
  RequestBody: JSON
      {"userId":"mansol","menuId":["1","2","3"]}
  Response: JSON
      {"status":"ok"}
```
> **DB**
```
  Table: order_info  
    - ukey: PK, unique key
    - userId: 주문요청 유저식별값
    - totalPrice: 총 결제금액
    - orderStatus: 주문상태(N:결제 전, Y:결제 완료)
    - regdate: 주문일자
    - paydate: 결제일자

CREATE TABLE std_order_info(
    ukey VARCHAR(20) NOT NULL,
    userId VARCHAR(20) NOT NULL,
    totalPrice INT,
    orderStatus VARCHAR(1),
    regdate DATETIME,
    paydate DATETIME,
CONSTRAINT std_order_info_PK PRIMARY KEY(ukey)
);

  Table: order_menu_info
    - ukey: PK, unique key
    - orderKey: FK, 주문키(order_info:ukey -> 1:N)
    - menuId: 주문요청 메뉴Id
    - price: 주문요청 메뉴가격

CREATE TABLE std_order_menu_info(
    ukey VARCHAR(20) NOT NULL,
    orderKey VARCHAR(20) NOT NULL,
    menuId VARCHAR(20),
    price INT,
CONSTRAINT std_order_menu_info_PK PRIMARY KEY(ukey),
CONSTRAINT std_order_menu_info_FK FOREIGN KEY(orderKey) REFERENCES std_order_info (ukey) 
);
```
> **로직**
```
  - 유저식별값으로 존재하는 유저인지 체크
  - 단수 혹은 복수의 메뉴선택 후 주문 요청
  - 주문상태 체크하여 결제 요청
  - 주문 완료 후 소유 포인트 체크하여 결제 금액보다 모자른 경우 결제 불가 -> 포인트 충전으로 이동
  - 주문 완료 후 소유 포인트 부족 상태에서 충전하지 않고 종료
  - 결제 완료 시 주문상태 및 결제일자 업데이트
```
## 인기메뉴 목록 조회 AP
> **API명세**
```
  POST /api/coffee/popularMenu/list
  RequestHeader: 
  RequestBody: JSON
      {"":"","":""}
  Response: JSON
      {"":""}
```
> **DB**
```
  -
``` 
> **로직**
```
  - 
```
