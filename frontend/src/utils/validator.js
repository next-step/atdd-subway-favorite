const validator = {
  stationName: [v => !!v || '이름 입력이 필요합니다.', v => v.length > 0 || '이름은 1글자 이상 입력해야 합니다.'],
  line: {
    name: [v => !!v || '이름 입력이 필요합니다.', v => v.length > 0 || '이름은 1글자 이상 입력해야 합니다.'],
    startTime: [v => !!v || '출발 시간 입력이 필요합니다.'],
    endTime: [v => !!v || '도착 시간 입력이 필요합니다.'],
    intervalTime: [v => !!v || '간격 입력이 필요합니다.'],
    color: [v => !!v || '색상 입력이 필요합니다.']
  },
  edge: {
    preStationId: [v => !!v || '이전역 id가 필요합니다.'],
    stationId: [v => !!v || '대상역 id가 필요합니다.'],
    distance: [v => !!v || '거리 입력이 필요합니다.', v => v >= 0 || '거리는 0 이상의 양수이어야 합니다.'],
    duration: [v => !!v || '시간 입력이 필요합니다.', v => v >= 0 || '시간은 0 이상의 양수이어야 합니다.']
  }
}

export default validator
