import {useEffect, useState} from 'react'

function App() {
    const [date, setDate] = useState(null)
    useEffect(() => {
        fetch("/api", {
            headers: {
                "Accept": "application/json"
            }
        })
            .then(res => res.json())
            .then(date => setDate(date))
    }, []);

    return (
        <section className="w-full h-screen flex flex-col gap-4 justify-center items-center bg-slate-900">
            <h2 className={'text-7xl font-bold tracking-[0.3em] bg-gradient-to-r from-purple-700 to-blue-700 bg-clip-text text-transparent'}>
                CANARY
            </h2>
            <article className="p-1.5 mt-8 rounded-lg shadow-lg bg-gradient-to-r from-purple-700 to-blue-700">
                <div className={'p-12 rounded-lg w-full h-full bg-slate-900'}>
                    <h1 className="text-5xl font-bold bg-gradient-to-r from-purple-700 to-blue-700 text-transparent bg-clip-text">
                        {date}
                    </h1>
                </div>
            </article>
        </section>
    )
}

export default App
