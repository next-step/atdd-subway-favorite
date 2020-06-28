import { EVENT_TYPE } from '../../utils/constants.js'
import { subwayLinesTemplate, colorSelectOptionTemplate } from '../../utils/templates.js'
import { subwayLineColorOptions } from '../../utils/defaultSubwayData.js'
import Modal from '../../ui/Modal.js'
import api from '../../api/index.js'
import { ERROR_MESSAGE } from '../../utils/constants.js'

function AdminLine() {
  const $subwayLineList = document.querySelector('#subway-line-list')
  const $subwayLineNameInput = document.querySelector('#subway-line-name')
  const $subwayLineStartTime = document.querySelector('#subway-start-time')
  const $subwayLineEndTime = document.querySelector('#subway-end-time')
  const $subwayIntervalTime = document.querySelector('#subway-interval-time')
  const $subwayLineFormSubmitButton = document.querySelector('#submit-button')
  const $subwayLineColor = document.querySelector('#subway-line-color')
  const $subwayLineAddButton = document.querySelector('#subway-line-add-btn')
  const subwayLineModal = new Modal()
  let isUpdateModal = false
  let $activeSubwayLineItem = null

  const createSubwayLine = async () => {
    const newSubwayLine = {
      name: $subwayLineNameInput.value,
      color: $subwayLineColor.value,
      startTime: $subwayLineStartTime.value,
      endTime: $subwayLineEndTime.value,
      intervalTime: $subwayIntervalTime.value
    }
    try {
      const newLine = await api.line.create(newSubwayLine)
      $subwayLineList.insertAdjacentHTML('beforeend', subwayLinesTemplate(newLine))
      subwayLineModal.toggle()
    } catch (e) {
      alert('에러가 발생했습니다.')
    }
  }

  const onDeleteSubwayLine = async (event) => {
    const $target = event.target
    try {
      const $subwayLineItem = $target.closest('.subway-line-item')
      const lineId = $subwayLineItem.dataset.lineId
      await api.line.delete(lineId)
      $subwayLineItem.remove()
    } catch (e) {
      alert('에러가 발생했습니다.')
    }
  }

  const onShowUpdateSubwayLineModal = async (event) => {
    const $target = event.target
    const isUpdateButton = $target.classList.contains('mdi-pencil')
    if (!isUpdateButton) {
      return
    }
    const $subwayLineItem = $target.closest('.subway-line-item')
    $activeSubwayLineItem = $subwayLineItem
    const $submitButton = document.querySelector('#submit-button')
    try {
      isUpdateModal = true
      const lineId = $subwayLineItem.dataset.lineId
      const line = await api.line.get(lineId)
      const { name, color, startTime, endTime, intervalTime } = line
      setSubwayLineForm(name, color, startTime, endTime, intervalTime)
      subwayLineModal.toggle()
      $submitButton.classList.add('update-submit-button')
    } catch (e) {
      alert(ERROR_MESSAGE.COMMON_FAIL)
    }
  }

  const updateSubwayLine = async () => {
    const updatedSubwayLine = getSubwayLineFormValue()
    try {
      await api.line.update($activeSubwayLineItem.dataset.lineId, updatedSubwayLine)
      subwayLineModal.toggle()
      const lines = await api.line.getAll()
      render(lines)
    } catch (e) {
      alert('업데이트에 실패했습니다.')
    }
  }

  const setSubwayLineForm = (name, color, startTime, endTime, intervalTime) => {
    $subwayLineNameInput.value = name || ''
    $subwayLineColor.value = color || ''
    $subwayLineStartTime.value = startTime || ''
    $subwayLineEndTime.value = endTime || ''
    $subwayIntervalTime.value = intervalTime || ''
  }

  const getSubwayLineFormValue = () => {
    return {
      name: $subwayLineNameInput.value,
      color: $subwayLineColor.value,
      startTime: $subwayLineStartTime.value,
      endTime: $subwayLineEndTime.value,
      intervalTime: $subwayIntervalTime.value
    }
  }

  const onSubmitHandler = (event) => {
    event.preventDefault()
    const $target = event.target
    isUpdateModal ? updateSubwayLine($target) : createSubwayLine()
  }

  const onSelectColorHandler = (event) => {
    event.preventDefault()
    const $target = event.target
    if ($target.classList.contains('color-select-option')) {
      document.querySelector('#subway-line-color').value = $target.dataset.color
    }
  }

  const initCreateSubwayLineForm = () => {
    const $colorSelectContainer = document.querySelector('#subway-line-color-select-container')
    const colorSelectTemplate = subwayLineColorOptions.map((option, index) => colorSelectOptionTemplate(option, index)).join('')
    $colorSelectContainer.insertAdjacentHTML('beforeend', colorSelectTemplate)
    $colorSelectContainer.addEventListener(EVENT_TYPE.CLICK, onSelectColorHandler)
  }

  const onCreateLineHandler = () => {
    isUpdateModal = false
    setSubwayLineForm()
  }

  const onUpdateLineWidgetView = async (event) => {
    try {
      const lineId = event.target.closest('.subway-line-item').dataset.lineId
      const { startTime, endTime, intervalTime } = await api.line.get(lineId)
      const $lineInfo = document.querySelector('.lines-info')
      $lineInfo.querySelector('.start-time').innerText = startTime
      $lineInfo.querySelector('.end-time').innerText = endTime
      $lineInfo.querySelector('.interval-time').innerText = intervalTime
    } catch (e) {
      alert(ERROR_MESSAGE.COMMON_FAIL)
    }
  }

  const onSubwayLineListHandler = (event) => {
    const $target = event.target
    const isDeleteButton = $target.classList.contains('mdi-delete')
    const isUpdateButton = $target.classList.contains('mdi-pencil')
    const isLineName = $target.classList.contains('line-name')

    if (isDeleteButton) {
      onDeleteSubwayLine(event)
    } else if (isUpdateButton) {
      onShowUpdateSubwayLineModal(event)
    } else if (isLineName) {
      onUpdateLineWidgetView(event)
    }
  }

  const render = (lines) => {
    $subwayLineList.innerHTML = lines.map(subwayLinesTemplate).join('')
  }

  const initEventListeners = () => {
    $subwayLineList.addEventListener(EVENT_TYPE.CLICK, onSubwayLineListHandler)
    $subwayLineFormSubmitButton.addEventListener(EVENT_TYPE.CLICK, onSubmitHandler)
    $subwayLineAddButton.addEventListener(EVENT_TYPE.CLICK, onCreateLineHandler)
  }

  const init = () => {
    initEventListeners()
    initCreateSubwayLineForm()
  }

  return {
    init
  }
}

const adminLine = new AdminLine()
adminLine.init()
