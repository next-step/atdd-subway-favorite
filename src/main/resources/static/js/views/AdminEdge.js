import { optionTemplate, subwayLineStationsTemplate } from '../../utils/templates.js'
import { EVENT_TYPE } from '../../utils/constants.js'
import Modal from '../../ui/Modal.js'
import api from '../../api/index.js'
import { ERROR_MESSAGE } from '../../utils/constants.js'

function AdminEdge() {
  const $subwayLineAddButton = document.querySelector('#subway-line-add-btn')
  const $createEdgeButton = document.querySelector('#create-edge-button')
  const $lineSelectOptions = document.querySelector('#line-select-options')
  const $lineSelectOptionsView = document.querySelector('#line-select-options-view')
  const $previousStationSelectOption = document.querySelector('#previous-station-select-options')
  const $nextStationSelectOptions = document.querySelector('#next-station-select-options')
  const $subwayLineDetail = document.querySelector('#subway-line-detail')
  const $distance = document.querySelector('#distance')
  const $duration = document.querySelector('#duration')
  const createSubwayEdgeModal = new Modal()

  const initSubwayLineOptions = async () => {
    try {
      const lines = await api.line.getAll()
      if (!lines) {
        return
      }
      const lineSelectTemplate = lines.map(optionTemplate).join('')
      $lineSelectOptionsView.innerHTML = lineSelectTemplate
      $lineSelectOptions.innerHTML = lineSelectTemplate
    } catch (e) {
      alert(ERROR_MESSAGE.COMMON_FAIL)
    }
  }

  const onCreateEdgeHandler = async (event) => {
    event.preventDefault()
    const lineId = $lineSelectOptions.value
    const newEdge = {
      preStationId: ($previousStationSelectOption && $previousStationSelectOption.value) || null,
      stationId: $nextStationSelectOptions.value,
      distance: $distance.value,
      duration: $duration.value
    }
    try {
      await api.line.addEdge(lineId, newEdge)
      createSubwayEdgeModal.toggle()
      await initSubwayLineView()
    } catch (e) {
      alert(ERROR_MESSAGE.COMMON_FAIL)
    }
  }

  const initSubwayLineView = async () => {
    try {
      const lineId = $lineSelectOptionsView.value
      const line = await api.line.get(lineId)
      $subwayLineDetail.innerHTML = subwayLineStationsTemplate(line)
    } catch (e) {
      alert(ERROR_MESSAGE.COMMON_FAIL)
    }
  }

  const onRemoveStationHandler = async (event) => {
    const $target = event.target
    const isDeleteButton = $target.classList.contains('mdi-delete')
    if (!isDeleteButton) {
      return
    }
    try {
      const lineId = $lineSelectOptionsView.value
      const stationId = $target.closest('.list-item').dataset.id
      await api.line.deleteStation(lineId, stationId)
      await initSubwayLineView()
    } catch (e) {
      alert(ERROR_MESSAGE.COMMON_FAIL)
    }
  }

  const initPreviousStationOptions = async () => {
    try {
      const line = await api.line.get($lineSelectOptions.value)
      if (line.stations) {
        const template = line.stations.map((edge) => optionTemplate(edge.station)).join('')
        $previousStationSelectOption.innerHTML = template
      }
    } catch (e) {
      alert(ERROR_MESSAGE.COMMON_FAIL)
    }
  }

  const initNextStationOptions = async () => {
    try {
      const stations = await api.station.getAll()
      $nextStationSelectOptions.innerHTML = stations.map(optionTemplate).join('')
    } catch (e) {
      alert(ERROR_MESSAGE.COMMON_FAIL)
    }
  }

  const initEdgeForm = async () => {
    await initPreviousStationOptions()
    await initNextStationOptions()
  }

  const initEventListeners = () => {
    $subwayLineAddButton.addEventListener(EVENT_TYPE.CLICK, initEdgeForm)
    $createEdgeButton.addEventListener(EVENT_TYPE.CLICK, onCreateEdgeHandler)
    $subwayLineDetail.addEventListener(EVENT_TYPE.CLICK, onRemoveStationHandler)
    $lineSelectOptionsView.addEventListener(EVENT_TYPE.CHANGE, initSubwayLineView)
    $lineSelectOptions.addEventListener(EVENT_TYPE.CHANGE, initEdgeForm)
  }

  const init = async () => {
    await initSubwayLineOptions()
    await initSubwayLineView()
    initEventListeners()
  }

  return {
    init
  }
}

const adminEdge = new AdminEdge()
adminEdge.init()
