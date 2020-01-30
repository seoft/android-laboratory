# drag-and-drop-between-multiple-grid


### 블로그
https://seoft.tistory.com/36


### 결과
![Android Emulator - Pixel_2_-_Nougat_5554 2020-01-30 21-00-53 mp4_20200130_214431](https://user-images.githubusercontent.com/55025826/73450960-d2007200-43a9-11ea-8fbd-a59d5519ca29.gif)


### 요구 사항
#### 기본적인 홈런쳐에 있는 기능과 유사하며, 요구사항은 다음과 같다.

- 9개의 아이템을 가지고 있는 4개의 그리드에 앱배치
- 각 그리드나 폴더 내 아이템 추가, 삭제
- 단순이동, 그리드간 이동
- 폴더 생성, 폴더 삭제
- 폴더 내의 아이템 각 그리드로 이동


### 구현 요약 
- 센터, 하단 그리드, 휴지통 위치정보 획득, 터치 정보 확보
- 센터 그리드 한칸당 3등분으로 분배
- 그리드에서 LongClick시 아이템 정보, 좌표 획득 후 동적으로 create, 현 터치지점 따라다니도록 구현
- 터치 리스너로 각 터치정보에 속하는지 판단 후 진행, 중간 중간에 플로팅뷰 동적 변경, saving할 상태 관리
  - 좌표 get, 3등분으로 분배한곳에 안속하는지(안속하면 retrun) 판단
  - Delete에 속하는지 판단
  - 리사이클러뷰 범위 밖인지 판단
  - Empty 판단
  - 정중앙 판단 with 폴더 insert, create
  - 0~1/3 or 2/3~1 범위 판단
  - 6번 범위시 가까운 empty index 파악 후 move
  - 하단 그리드 속하는지 판단
  - 터치 up시 삭제범위 속 or 그리드 초과범위 판단
  - 폴더, 안폴더시 배치 처리
  - 폴더이고 이동 배치 완료시 finish 처리
  - 센터, 하단 그리드 갱신
- 폴더 처리시 중복사용 코드가 많아 상속을 쓰려다 초반 설계에서 잘못됬다는걸 깨닫고 편법사용
- static 변수와 같은 엑티비티 하나 더뛰어 폴더유무 판단
- 폴더, Empty, 기본앱 유무를 상위 클래스를 두어 공통으로 관리
- json list <-> object list로 preference로 관리

### Reference
- https://seoft.tistory.com/35?category=1066264
- https://seoft.tistory.com/33?category=1066264
- https://seoft.tistory.com/32?category=1066264