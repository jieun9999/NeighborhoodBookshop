# NeighborhoodBookshop

책으로 모이는 사람들, 동네책방  

  

# 1. 회원가입, 로그인
- 쉐어드 프리퍼런스


https://github.com/jieun9999/NeighborhoodBookshop/assets/112951633/0a254291-dc7e-41f5-9690-cce23d768694


(1) 이름, 아이디 , 비밀번호를 입력해서 회원가입을 합니다. 이것은 '가입정보' 쉐어드에 저장됩니다.
(2) '가입정보' 쉐어드에 회원가입 정보가 있다면, 로그인이 가능합니다

# 2. 카카오 로그인 
- 카카오톡 api, 쉐어드 프리퍼런스

(1) 카카오톡 api에서 userID를 받아 '가입정보' 쉐어드에 저장합니다.
(2) 카카오톡 api에서 유저의 이미지를 받아옵니다. 
   
# 3. 프로필 등록 
 - 구글 맵 api, 쉐어드 프리퍼런스

(1) 이미지 : 카카오톡으로 이미지를 받아올 경우(웹 경로) glide를 사용하고, 갤러리에서 이미지를 받아올 경우 bitmap을 사용합니다.
(2) 위치정보 : 구글맵 gps 기능을 사용해 유저의 위치 정보를 등록합니다.
(3) 인사말, 인스타 url : 인사말과 인스타 url을 등록합니다.
   
# 4. 독후감 crud 
- 인텐트, 리사이클러뷰, 쉐어드 프리퍼런스

(1) 등록하기:
(2) 상세보기:
(3) 삭제하기:
(4) 삭제하기:
   
# 5. 지도 기반으로 다른 유저 확인 
- 구글 맵 api
   
# 6. 책 이름으로 리뷰 검색하기
- 리사이클러뷰, 쉐어드 프리퍼런스
   
# 7. 다른 유저의 리뷰에 좋아요/ 댓글 달기 
- 리사이클러뷰, 쉐어드 프리퍼런스
   
# 8. 북클럽 crud 
- 리사이클러뷰
   
# 9. 독서시간 기록하기, 명언 배너
- 핸들러
