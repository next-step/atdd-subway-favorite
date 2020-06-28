const METHOD = {
  PUT(data) {
    return {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
  POST(data) {
    return {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({
        ...data
      })
    }
  },
  DELETE() {
    return {
      method: 'DELETE'
    }
  }
}

const api = (() => {
  const request = (uri, config) => fetch(uri, config)

  const requestWithReturn = (uri, config) => request(uri, config).then((data) => data.json())

  const station = {
    get(id) {
      return requestWithReturn(`/stations/${id}`)
    },
    getAll() {
      return requestWithReturn(`/stations`)
    },
    create(data) {
      return requestWithReturn(`/stations`, METHOD.POST(data))
    },
    update(data, id) {
      return request(`/stations/${id}`, METHOD.PUT(data))
    },
    delete(id) {
      return request(`/stations/${id}`, METHOD.DELETE())
    }
  }

  const line = {
    get(id) {
      return requestWithReturn(`/lines/${id}`)
    },
    getAll() {
      return requestWithReturn(`/lines`)
    },
    create(data) {
      return requestWithReturn(`/lines`, METHOD.POST(data))
    },
    update(id, data) {
      return request(`/lines/${id}`, METHOD.PUT(data))
    },
    delete(id) {
      return request(`/lines/${id}`, METHOD.DELETE())
    },
    deleteStation(lineId, stationId) {
      return request(`/lines/${lineId}/stations/${stationId}`, METHOD.DELETE())
    },
    addEdge(id, data) {
      return request(`/lines/${id}/stations`, METHOD.POST(data))
    }
  }

  return {
    station,
    line
  }
})()

export default api
