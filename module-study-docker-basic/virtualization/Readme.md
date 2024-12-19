## 가상화 기술

-------

### 서버란 ? - 하드웨어에서 실행 중인 소프트웨어

### 엔터프라이즈 서버 운영 방식

- **베어메탈** : 하나의 OS 에서 여러 서버를 운영
- **하이퍼바이저** : 하나의 OS(호스트 OS) 에서 서로 격리된 `가상의 OS(게스트 OS)`를 활용하여 여러 서버를 운영
- **컨테이너** : 하나의 OS 에서 서로 격리된 여러 서버를 운영, `리눅스 커널의 자체 격리 기술`을 사용

> ✅ 하이퍼바이저 방식은 게스트 OS에서 발생한 System Call은 **호스트 OS의 System Call을 호출**하게 된다.
> <br>
> <br>
> ✅ 컨테이너 가상화와 하이퍼바이저 방식의 가장 큰 `차이점`으로 **컨테이너 가상화는 호스트 OS의 커널을 모든 컨테이너가 공유**한다.
