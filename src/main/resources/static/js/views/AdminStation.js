import { EVENT_TYPE, ERROR_MESSAGE, INSERT_HTML_POSITION } from '../../utils/constants.js'
import { listItemTemplate } from '../../utils/templates.js'
import api from '../../api/index.js'
import { isEnterKey } from '../../utils/validator.js'

function AdminStation() {
  const $stationInput = document.querySelector('#station-name')
  const $stationList = document.querySelector('#station-list')
  const $stationAddButton = document.querySelector('#station-add-btn')

  const onAddStationHandler = async (event) => {
    if (event.key && !isEnterKey(event)) {
      return
    }
    event.preventDefault()
    const $stationNameInput = document.querySelector('#station-name')
    const stationName = $stationNameInput.value
    if (!stationName) {
      alert(ERROR_MESSAGE.NOT_EMPTY)
      return
    }
    try {
      const newStation = await api.station.create({ name: stationName })
      $stationNameInput.value = ''
      $stationList.insertAdjacentHTML(INSERT_HTML_POSITION.BEFORE_END, listItemTemplate(newStation))
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
      const targetStation = $target.closest('.list-item')
      const stationId = targetStation.dataset.id
      await api.station.delete(stationId)
      targetStation.remove()
    } catch (e) {
      alert(ERROR_MESSAGE.COMMON_FAIL)
    }
  }

  const initEventListeners = () => {
    $stationInput.addEventListener(EVENT_TYPE.KEY_PRESS, onAddStationHandler)
    $stationList.addEventListener(EVENT_TYPE.CLICK, onRemoveStationHandler)
    $stationAddButton.addEventListener(EVENT_TYPE.CLICK, onAddStationHandler)
  }

  const init = () => {
    initEventListeners()
  }

  return {
    init
  }
}

const adminStation = new AdminStation()
adminStation.init()
