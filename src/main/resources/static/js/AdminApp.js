import { initNavigation } from '../utils/templates.js'

function AdminApp() {
  const init = () => {
    initNavigation()
  }

  return {
    init
  }
}

const adminApp = new AdminApp()
adminApp.init()
