import { useState, useEffect, useRef } from 'react'
import { CheckCircle, X as XIcon } from 'lucide-react'

export default function Toast({ show, message, type = 'success' }) {
  const [render, setRender] = useState(false)
  const [visible, setVisible] = useState(false)
  const timerRef = useRef(null)

  useEffect(() => {
    if (show) {
      clearTimeout(timerRef.current)
      setRender(true)
      requestAnimationFrame(() => {
        requestAnimationFrame(() => {
          setVisible(true)
        })
      })
    } else {
      setVisible(false)
    }
  }, [show])

  useEffect(() => {
    if (render && !visible && !show) {
      timerRef.current = setTimeout(() => {
        setRender(false)
      }, 400)
      return () => clearTimeout(timerRef.current)
    }
  }, [render, visible, show])

  if (!render) return null

  const isError = type === 'error'

  return (
    <div
      className={`
        fixed top-5 right-5 z-[100] flex items-center gap-3
        bg-white border shadow-2xl rounded-2xl px-6 py-4
        transition-all duration-[400ms] ease-out pointer-events-none
        ${isError ? 'border-red-200' : 'border-emerald-200'}
        ${visible
          ? 'translate-x-0 opacity-100 scale-100'
          : 'translate-x-[calc(100%+2rem)] opacity-0 scale-95'
        }
      `}
    >
      <div className={`w-10 h-10 rounded-full flex items-center justify-center shrink-0 ${isError ? 'bg-red-50 text-red-600' : 'bg-emerald-50 text-emerald-600'}`}>
        {isError ? <XIcon className="w-6 h-6" /> : <CheckCircle className="w-6 h-6" />}
      </div>
      <div>
        <h4 className="font-semibold text-slate-800 text-sm whitespace-nowrap">{isError ? 'Error' : 'Operación Completada'}</h4>
        <p className="text-xs text-slate-500">{message}</p>
      </div>
    </div>
  )
}
