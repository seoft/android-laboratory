# write-post-with-items


## 블로그
https://seoft.tistory.com/50


## 결과
![Android Emulator - Pixel_2_P_5554 2020-05-16 20-08-16 mp4_20200516_200927](https://user-images.githubusercontent.com/55025826/82119038-31965800-97b6-11ea-864b-2df61ffcde53.gif)   ![Android Emulator - Pixel_2_P_5554 2020-05-16 20-13-59 mp4_20200516_201501](https://user-images.githubusercontent.com/55025826/82119039-32c78500-97b6-11ea-9ecf-3ad22c1b397e.gif)




## 요구 사항
#### 모바일용 글 작성페이지로 요구사항은 다음과 같다.
- 글 작성 외에도 사진, 투표, to-do, 유튜브를 포함 (투표가 to-do 상위호환이고 유튜브는 url 데이터 정의만 하면 되기 때문에 구현에서 제외하고 투표만 구현)
- 글 작성페이지에 다수의 복합적인 아이템(사진,투표 등) 배치
- 아이템 중간 중간에도 글 작성
- 아이템 삭제, 위치 변경

#### 투표 작성 페이지의 요구사항은 다음과 같다.
- 글, 이미지가 투표 항목 중 하나로 정의
- 투표 항목 삭제, 위치 변경 가능
- 투표 저장 후 변경을 위한 재진입시 내용 유지


## 구현 요약 
- MVVM, 데이터 바인딩 구조
- 이미지 파일 load
- 리사이클러뷰 순서 변경(with editText 내용유지)
  - editText 리스트의 스크롤시 기존 데이터 save,load와 swap 방식 :
  - xml에서 onTextChanged를 통해 [contents] 를 지속적으로 갱신(뷰업데이트는 제외)
  - add, remove, swap 경우 [contents] 변경 후 -> [contentItems] -> [refreshView]
  - xml에서 focus 변경 경우 기존 [contents] -> [contentItems] -> [refreshView] 
- 리스트 관리 : SelaedClass , ListAdpater, ItemTouchHelper
