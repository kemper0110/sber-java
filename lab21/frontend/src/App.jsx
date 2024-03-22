import {useEffect, useState} from 'react'

async function loader() {
    const res = await fetch("/api", {
        headers: {
            "Accept": "application/json"
        }
    })
    return await res.json()
}

function App() {
    const [date, setDate] = useState(null)
    useEffect(() => {
        loader().then(res => setDate(res))
    }, []);

    const version = 'CANARY 1.4'
    return (
        <section className="w-full h-screen flex flex-col gap-4 justify-center items-center bg-slate-900">
            {
                date ? (
                    <>
                        <h2 className={'text-center text-7xl font-bold tracking-[0.3em] bg-gradient-to-r from-purple-700 to-blue-700 bg-clip-text text-transparent'}>
                            {version}
                        </h2>
                        <article
                            className="p-1.5 mt-8 rounded-lg shadow-lg bg-gradient-to-r from-purple-700 to-blue-700">
                            <div className={'p-12 rounded-lg w-full h-full bg-slate-900'}>
                                <h1 className="text-5xl font-bold bg-gradient-to-r from-purple-700 to-blue-700 text-transparent bg-clip-text">
                                    {date}
                                </h1>
                            </div>
                        </article>
                    </>
                ) : null
            }
        </section>
    )
}


export default App
