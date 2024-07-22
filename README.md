# ❗ 리팩토링
ㅤ(기존 코드 - https://github.com/kuseulki/Project_Market)
- 기존 코드에서 중복된 코드와 잘못된 코드를 수정하였습니다.
- 각 계층에 맞는 DTO를 새로 정의 하여 설계와 유지 보수가 용이하고 계층의 역할과 책임을 명확하게 구분하였습니다.
- 아임포트를 이용하여 상품 결제 부분을 추가하였습니다.
- 해시태그 기능을 추가하여 해시태그로 검색구현 가능하도록 하였습니다.

ㅤㅤ
---
ㅤ
### 목차
[1. 프로젝트 소개](#1-프로젝트-소개)

[2. 개발 환경](#2-개발-환경)

[3. 기술 세부 스택](#3-기술-세부-스택)

[4. ERD 구조](#4-erd-구조)

[5. 주요기능](#5-주요기능)

[6. API 명세](#6-api-명세)

ㅤ
ㅤ
---
ㅤ
## 1. 프로젝트 소개
 - 상품을 구매하고 결제 할 수 있으며, 게시글 및 댓글을 작성할 수 있는 웹 쇼핑몰 입니다. SpringBoot와 Spring Data JPA를 사용하여 기본적인 REST API를 구현하였고, 관리자와 구매자로 권한을 따로 두어 구현 하였습니다.

- 개발인원 : 1명
- 개발기간: 23.04 ~ 23.07
ㅤ
---
ㅤ
## 2. 개발 환경

- 스프링부트 버전 : 3.2.5
- JDK 버전 : JDK 17
- 데이터 베이스 : mysql
- 빌드 툴 : Gradle
- JAVA17
ㅤ
---
ㅤ
## 3. 기술 세부 스택
ㅤ
* Spring Boot
* Spring Web
* Spring Data JPA
* Thymeleaf
* Spring Security
* H2 Database
* MySQL Driver
* Lombok
* Spring Boot DevTools
* Spring validation
* oauth2
* Bootstrap
ㅤ
---
ㅤ
## 4. ERD 구조
<img width="662" alt="erd" src="https://github.com/user-attachments/assets/02f340d4-2a20-47b4-bb44-eb3db47b04b9">
ㅤ

---

## 5. 주요기능
ㅤ
- Security / OAuth2.0 회원가입/ 로그인 기능
- 아이디 중복 체크 기능
- 카카오 주소 API 검색 기능
- 게시글에 파일 첨부 기능
- 댓글 기능
- 게시글 제목/작성자 조회 기능
- 상품 구매 / 조회 / 결제 / 카테고리별 상품 / 장바구니/ 찜 기능
- 게시글 / 상품 페이징 / 검색 기능
ㅤ
### [ 공통 기능 ]
ㅤ

**회원가입, 로그인**
ㅤ
- 스프링 시큐리티 / OAuth2.0을 이용 회원가입과 로그인을 할 수 있다.
- 사용자는 아이디, 비밀번호, 이름, 휴대폰, 주소를 이용해 회원가입을 할 수 있다.
- 회원가입 시 유효성 검사 / 아이디 중복 체크를 통과해야 한다.
- 회원가입 시 주소는 카카오 주소 API를 이용한다.
ㅤ

### [ 사용자 기능 ]
ㅤ

**게시판 ( Q & A )**
ㅤ
- 게시판에 글(이미지 파일 첨부 가능)과 댓글을 작성할 수 있다.
- 본인이 작성한 글만 수정, 삭제가능 하다
- 본인이 작성한 댓글만 삭제 가능 하다.
- 제목과 작성자로 검색 할 수 있다.
- 게시글을 제목, 날짜, 이름 순으로 조회 할 수 있다.
- 페이징 기능으로 게시글을 볼 수있다.
ㅤ

**마이페이지**
ㅤ
- 나의 회원정보를 수정할 수 있다.
- 내가 작성한 글만 볼 수 있다.
- 장바구니 / 위시리스트 / 내가 주문한 내역을 볼 수 있다.
ㅤ

**상품**
ㅤ
- 상품을 카테고리별로 조회 할 수 있다.
- 상품을 조회/구매/결제/검색/장바구니/찜 할 수 있다
 ㅤ   
 **장바구니**
ㅤ
- 장바구니에 상품을 추가한 후, 상품의 수량을 조절하거나 상품을 삭제할 수 있다.
- 중복된 상품을 장바구니에 추가 시 상품 수량만 증가 한다.
- 체크한 상품의 수량과 가격 으로 총 합계를 계산하여 보여준다.
ㅤ
### [ 관리자 기능 ]
ㅤ

**상품 / 카테고리**
ㅤ
- 상품 등록시 유효성 검사를 하며, 수정/ 삭제 할 수있다.
- 상품 등록시 상품 사진은 여러장 등록 할 수있고, 첫번째 사진은 대표 사진으로 등록된다.
- 상품 전체 목록을 조회할 수 있고, 상품 하나의 정보도 조회할 수 있다.
- 상품의 품절 여부를 변경할 수 있다.
- 상품 카테고리를 등록/수정/삭제할 수 있다.
- 등록된 상품 전체 조회 및 재고를 확인 할 수 있다.
- 가입한 회원을 전체 조회 할 수 있다.
- 상품의 이름으로 검색 할 수 있다.
ㅤ
**주문 관리**
ㅤ
- 결제를 하면 장바구니에 담겨있는 모든 상품이 주문 내역으로 생성된다.
- 주문한 상품들은 조회할 수 있다.
ㅤ
**주문 관리**
- 포트을 이용하여 상품을 결제 할 수 있다.
ㅤ
---
ㅤ
## 6. API 명세
ㅤ
### 회원

| Domain | URL | Method | 설명 | 접근권한 |
| --- | --- | --- | --- | --- |
|  | /join | Get  Post | 회원가입 페이지 |  |
|  | /member/id-check | Post | 아이디 중복 체크 |  |
|  | /member/login | Get   | 로그인 페이지 |  |
|  | /member/login/error | Get   | 로그인 오류 페이지 |  |
| MyPage | /member/mypage/article |  Get  | 내가 작성한 글 보 |  |
ㅤ
### 관리자

| Domain | URL | Method | 설명 | 접근권한 |
| --- | --- | --- | --- | --- |
| Admin | /admin/page | Get  | 관리자 페이지 | ADMIN |
|  | /admin/item/all | Get | 전체 상품 관리 | ADMIN |
|  | /admin/user/all | Get   | 전체 회원 관리 | ADMIN |
ㅤ
### **게시글 / 댓글**
ㅤ
| Domain | URL | Method | 설명 | 접근권한 |
| --- | --- | --- | --- | --- |
| Article | /articles | Get | 게시글 목록 조회 |  |
|  | /articles/form | Get  Post | 게시글 저장 |  |
|  | /articles/articleId | Get   | 게시글 상세보기 |  |
|  | /articles/articleId/form | Get  Post | 게시글 수정 |  |
|  | /articles/articleId/delete | Post | 게시글 삭제 |  |
|  | /articles/search-hashtag |  | 게시글 해시태그 검색 |  |
| Comment | /comments/new | Post | 댓글 작성 |  |
|  | /comments/{commentId}/delete | Post | 댓글 삭제 |  |
ㅤ
### 상품

| Domain | URL | Method | 설명 | 접근권한 |
| --- | --- | --- | --- | --- |
| Item | /item | Get | 상품 목록 조회 |  |
|  | /item/form | Get  Post | 상품 저장 | ADMIN |
|  | /item/{itemId} | Get   | 상품 상세보기 |  |
|  | /item/{itemId}/update | Get  Post | 상품 수정 | ADMIN |
|  | /item/{itemId}/delete | Post | 상품 삭제 | ADMIN |
|  | /item/search-hashtag |  | 상품 해시태그 검색 |  |
| Category | /categories | Get  Post | 카테고리 생성 | ADMIN |
|  | /categories/list | Get | 카테고리 목록 | ADMIN |
ㅤ
### 상품

| Domain | URL | Method | 설명 | 접근권한 |
| --- | --- | --- | --- | --- |
| Cart | /cart | Post | 장바구니로 상품 이동 |  |
|  | /cart/list | Get  | 장바구니 목록 페이지 |  |
|  | /cart/{cartItemId} | Patch | 장바구니  수량 업데이트 |  |
|  | /cart/{cartItemId} | Delete | 장바구니 삭제 |  |
| Order | /cart/orders | Post | 장바구니 상품 주문으로 이동 |  |
|  | /order | Post | 주문 생성  |  |
|  | /orders | Get   | 주문 목록 페이지 |  |
| WishList | /wishlist/list | Get | 찜하기 목록 페이지 |  |
|  | /wishlist | Post | 찜하기 에게 상품 추가 |  |
|  | /wishlist/{wishListItemId} | Post | 찜하기 에서 상품 삭제 |  |
| Payment | /payment | Post | 결제 페이지로 이동 |  |
|  | /payment/{id} | Get | 결제 |  |
|  | /success-payment | Get | 결제 성공 시 이동 |  |
|  | /fail-payment | Get | 결제 실패 시 이동 |  |
