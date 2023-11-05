# NeighborhoodBookshop

책으로 모이는 사람들, 동네책방  


## 0. 데이터 설계
<img width="2227" alt="data_최종" src="https://github.com/jieun9999/NeighborhoodBookshop/assets/112951633/557d8632-21cd-4525-b7ae-654089b97029">
  

## 1. 회원가입, 로그인

https://github.com/jieun9999/NeighborhoodBookshop/assets/112951633/4041a695-ff4f-4340-8fe0-6155b196f1fd


- 이름, 아이디 , 비밀번호를 입력해서 회원가입을 합니다. 이것은 '가입정보' 쉐어드에 저장됩니다.
- '가입정보' 쉐어드에 회원가입 정보가 있다면, 로그인이 가능합니다


  > 주요기법: 쉐어드 프리퍼런스




## 2. 카카오 로그인 

https://github.com/jieun9999/NeighborhoodBookshop/assets/112951633/3dd9faa0-eb11-4557-84fb-19f190479989


- 카카오톡 api에서 유저의 ID를 받아 '가입정보' 쉐어드에 저장합니다.
- 카카오톡 api에서 유저의 이미지를 받아옵니다. 

  > 주요기법: 카카오톡 api, 쉐어드 프리퍼런스




   
## 3. 프로필 등록 

https://github.com/jieun9999/NeighborhoodBookshop/assets/112951633/b195a25d-d5f3-4770-b022-2e926945e455


(1) 이미지 : 카카오톡으로 이미지를 받아올 경우(웹 경로) glide를 사용하고, 갤러리에서 이미지를 받아올 경우 bitmap을 사용합니다.
(2) 위치정보 : '나의 위치 가져오기' 버튼을 누르면, 구글맵 gps 기능을 사용해 유저의 위치 정보를 등록합니다.
(3) 인사말, 인스타 url : 인사말과 인스타 url을 등록합니다.

  > 주요기법: 구글 맵 api, 쉐어드 프리퍼런스




   
## 4. 책리뷰 crud 



https://github.com/jieun9999/NeighborhoodBookshop/assets/112951633/59bdd23a-ee9c-4575-b21c-9a4413647909




- 등록하기: 책 관련 정보(책이름, 저자, 출판사, 출판일, ISBN)과 감상평(별점, 메모)를 등록합니다.
- 상세보기: 등록한 책리뷰의 전체 내용을 확인할 수 있습니다.
- 수정하기: '⋮' 버튼을 클릭하면, 모달이 뜨고, 감상평을 수정할 수 있습니다.
- 삭제하기: '⋮' 버튼을 클릭하면, 모달이 뜨고, 책리뷰를 삭제할 수 있습니다.

 > 주요기법:  인텐트, 리사이클러뷰, 쉐어드 프리퍼런스




   
## 5. 지도 기반으로 다른 유저 확인 

https://github.com/jieun9999/NeighborhoodBookshop/assets/112951633/565e02dd-d883-4959-b917-44f50e15a286


- 내 주변의 유저들을 구글맵 상에서 확인할 수 있습니다.
- 프로필 마커를 누르면, 해당 유저의 프로필 바텀 시트가 올라옵니다.

> 주요기법: 구글 맵 api




   
## 6. 책 이름으로 리뷰 검색하기

https://github.com/jieun9999/NeighborhoodBookshop/assets/112951633/72ad8cde-0a1b-4ee0-a381-cbfbe3027cc0

- 예를들어, '도' 라고 입력하면 '도둑맞은 집중력' 책리뷰 리스트가 뜹니다. 
- 예를들어, '총'이라고 입력하면, '총균쇠' 책리뷰 리스트가 뜹니다.

> 주요기법: 리사이클러뷰, 쉐어드 프리퍼런스





## 7. 다른 유저의 리뷰에 좋아요/ 댓글 달기 

https://github.com/jieun9999/NeighborhoodBookshop/assets/112951633/c64a313a-6a62-4266-aadd-8ce3d25f564f

- 다른 유저의 프로필과 책리뷰를 확인할 수 있습니다.
- 좋아요를 누르거나, 댓글을 달 수 있습니다. 다른 유저의 댓글 또한 확인할 수 있습니다.
- 쉐어드에 처음 댓글이 생성된 시간도 저장하기 때문에 '몇분 전', '몇 시간 전' 과 같은 댓글 기록이 뜹니다.

> 주요기법:  리사이클러뷰, 쉐어드 프리퍼런스




   
## 8. 북클럽 crud 

https://github.com/jieun9999/NeighborhoodBookshop/assets/112951633/e042817e-2cc8-4b0b-8895-288d39e32600

- 등록하기: 북클럽 카드를 누르면, 바텀시트를 통해 모임 세부 정보를 확인할 수 있습니다.
- 상세보기:'➕' 버튼을 누르면, 북클럽명, 북클럽 이미지, 온라인 여부, 북클럽 소개, 카테고리 를 입력할 수 있습니다.
- 수정하기: 연필 아이콘을 누르면, 북클럽 내용을 수정할 수 있습니다.
- 삭제하기: '✖️' 아이콘을 누르면, 북클럽 카드를 삭제할 수 있습니다.

> 주요기법: 리사이클러뷰




   
## 9. 독서시간 기록하기, 명언 배너

https://github.com/jieun9999/NeighborhoodBookshop/assets/112951633/7676e797-290b-414e-8dac-be2ab9da650e

- 재생 버튼을 누르면, 독서시간을 잴 수 있습니다.
- 리셋 버튼을 누르면, 0초로 초기화 됩니다.
- 3초마다 다른 배너가 등장합니다.

> 주요기법: 핸들러




