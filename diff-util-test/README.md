# diff-util-test


### 세부 내용 블로그에 기술 :
https://blog.seoft.co.kr/86


### 스크린샷
![image](https://user-images.githubusercontent.com/55025826/135757192-54c6c74f-8b7c-4627-94bd-89a6fb0c5824.png)


### 테스트 환경
 - 현 시점에 적당히 마지노선의 단말기(?)인 갤럭시 S8(OS 9)로 테스트
 - 기존 테스트의 UUID 보다는 실제 유즈케이스와 더 밀접한 환경(?)
 - 멀티타입의 뷰홀더
 - 실제 모델 id값으로 areItemsTheSame을, ui 변경대상 파라미터만으로 areContentsTheSame 로 비교
 - payload 전달로 뷰 부분 업데이트 진행
 - 테스트 케이스 : 순차적 추가, 랜덤 추가, 삭제, 셔플, 수정, (단일 추가,수정,삭제)
 - 테스트 개수 범위 : 200*n (~1000) , 5000*n (~50000)
 - 단일 업데이트 경우 인덱스 마지막 부근에서 진행


### 결과
![image](https://user-images.githubusercontent.com/55025826/135757024-c1c4fa62-6032-49d4-8d18-b3ab682a06eb.png)
세부사항 엑셀 : android-laboratory\diff-util-test\diffutil_test_result.xlsx