import {baseUrl} from "@/config";
import Link from "next/link";
import {redirect} from "next/navigation";

export type CreateData = {
    ingredients: { id: number, name: string }[]
}

async function getData(): Promise<CreateData> {
    const res = await fetch(`${baseUrl}/receipt/create`, {
        cache: "no-store"
    })

    if (!res.ok) {
        throw new Error('Failed to fetch data: ' + res.url)
    }

    return res.json()
}


export default async function Create() {
    const data = await getData()
    const {ingredients} = data

    async function submit(formData: FormData) {
        "use server"

        const ingredients = formData.getAll("ingredient[]")
            .map(id => ({
                id, quantity: formData.get(`quantity-${id}`)
            }))
        const form = {
            name: formData.get('name'),
            ingredients
        }
        console.log(form)
        const res = await fetch(`${baseUrl}/receipt`, {
            method: "POST",
            cache: "no-store",
            body: JSON.stringify(form),
            headers: {
                'Content-Type': "application/json"
            }
        })
        if (!res.ok) {
            throw new Error('Failed to fetch data: ' + res.url)
        }
        redirect("/")
    }

    return (
        <main className="max-w-[800px] mx-auto flex min-h-screen flex-col items-center pt-24">
            <form action={submit} className={'w-full'}>
                <input className={'w-full px-3 py-1.5 rounded-lg shadow-xl focus:outline-none text-xl'}
                       placeholder={'Название'}
                       autoComplete={'receipt-name'}
                       name={'name'}
                />
                <div className={'mt-8 flex gap-x-2 gap-y-4 flex-wrap'}>
                    {
                        ingredients.map(({id, name}) => (
                            <label key={id}>
                                <input type={'checkbox'} name={`ingredient[]`} defaultValue={id} className={'[&:checked~span]:bg-slate-400 [&:checked~span>input]:block'}
                                       hidden/>
                                <span
                                    className={'flex gap-2 px-3 py-1.5 text-xl bg-slate-300 cursor-pointer rounded-md shadow-md'}>
                                    <span>
                                        {name}
                                    </span>
                                    <input
                                        className={'hidden w-[50px] bg-slate-300 rounded focus:outline-none'}
                                        min={1} max={100} type={'number'} name={`quantity-${id}`} defaultValue={1}/>
                                </span>
                            </label>
                        ))
                    }
                </div>
                <div className={'mt-4 flex justify-end gap-4'}>
                    <Link role={'button'} href={'/'}
                          className={'flex items-center transition-colors bg-slate-100 hover:bg-slate-300 px-8 py-1 shadow-md rounded-xl'}>
                        <ArrowBackIcon/>
                    </Link>
                    <button
                        className={'text-xl font-medium transition-colors bg-slate-100 hover:bg-slate-300 px-8 py-1.5 shadow-md rounded-xl'}
                        type={'submit'}
                    >
                        Сохранить
                    </button>
                </div>
            </form>
        </main>
    )
}

const ArrowBackIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" className="icon icon-tabler icon-tabler-arrow-back-up"
         width="32" height="32" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" fill="none"
         strokeLinecap="round" strokeLinejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
        <path d="M9 14l-4 -4l4 -4"/>
        <path d="M5 10h11a4 4 0 1 1 0 8h-1"/>
    </svg>
)