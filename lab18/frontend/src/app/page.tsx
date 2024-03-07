import {redirect, RedirectType} from "next/navigation";
import Link from "next/link";
import {baseUrl} from "@/config";
import {SearchedText} from "@/components/SearchedText";

type IndexData = {
    receipts: Receipt[]
}

type Receipt = {
    id: number
    name: string
    ingredients: string[]
}

async function getData({name}: { name: string }): Promise<IndexData> {
    const res = await fetch(`${baseUrl}/receipt?` + new URLSearchParams({name}), {
        cache: "no-store"
    })

    if (!res.ok) {
        throw new Error('Failed to fetch data: ' + res.url)
    }

    return res.json()
}

export default async function Home({searchParams}: {
    searchParams: { [key: string]: string | string[] | undefined }
}) {
    const name = (
        Array.isArray(searchParams.name) ? searchParams.name[0] : searchParams.name
    ) ?? ""
    const {receipts} = await getData({name})

    async function search(formData: FormData) {
        'use server';
        const name = formData.get("name")
        redirect("/?" + new URLSearchParams({
            name: typeof name === 'string' ? name : ""
        }), RedirectType.push)
    }

    async function deleteReceipt(id: number) {
        'use server';
        const res = await fetch(`${baseUrl}/receipt/${id}`, {
            method: "DELETE",
            cache: "no-store"
        })
        if (!res.ok) {
            throw new Error('Failed to fetch data: ' + res.url)
        }
        const deleted = await res.text()
        console.log({id, deleted})
        redirect("/")
    }

    return (
        <main className="max-w-[800px] mx-auto flex min-h-screen flex-col items-center pt-24">
            <div className={'w-full flex justify-end'}>
                <Link href={"/create"} role={'button'}
                      className={'transition-colors bg-slate-100 hover:bg-slate-300 px-8 py-1 shadow-md rounded-xl'}>
                    <svg xmlns="http://www.w3.org/2000/svg" width="40" height="40"
                         viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" fill="none"
                         strokeLinecap="round" strokeLinejoin="round">
                        <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
                        <path d="M12 5l0 14"/>
                        <path d="M5 12l14 0"/>
                    </svg>
                </Link>
            </div>
            <div className="mt-6 w-full flex place-items-center justify-center">
                <form action={search} className={'w-full flex items-center bg-white px-3 py-1.5 rounded-lg shadow-xl'}>
                    <input defaultValue={name} name={'name'} className={'w-full focus:outline-none text-xl'}/>
                    <button type={'submit'}>
                        <SearchIcon/>
                    </button>
                </form>
            </div>
            <div className={'w-full mt-12 flex flex-col gap-4'}>
                {
                    receipts.map(receipt => (
                        <article key={receipt.id}
                                 className={'w-full flex items-start justify-between bg-white px-3 py-1.5 rounded-lg shadow-md gap-4'}>
                            <h1 className={'mt-1 shrink-0'}>
                                {
                                    name && name !== '' ?
                                        <SearchedText text={receipt.name} needle={name}/>
                                        : receipt.name
                                }
                            </h1>
                            <p className={'grow shrink truncate flex gap-2 flex-wrap'}>
                                {
                                    receipt.ingredients.map(ing => (
                                        <span key={ing} className={'rounded-xl px-2 py-1 bg-slate-200'}>
                                            {ing}
                                        </span>
                                    ))
                                }
                            </p>
                            <form action={deleteReceipt.bind(null, receipt.id)}>
                                <button type={'submit'} className={'mt-1 shrink-0 text-red-600'}>
                                    <XMark/>
                                </button>
                            </form>
                        </article>
                    ))
                }
            </div>
        </main>
    );
}

const SearchIcon = () => (
    <svg xmlns="http://www.w3.org/2000/svg" className="icon icon-tabler icon-tabler-search" width="24"
         height="24" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor" fill="none"
         strokeLinecap="round" strokeLinejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
        <path d="M10 10m-7 0a7 7 0 1 0 14 0a7 7 0 1 0 -14 0"/>
        <path d="M21 21l-6 -6"/>
    </svg>
)
const XMark = () => (
    <svg xmlns="http://www.w3.org/2000/svg" className="icon icon-tabler icon-tabler-x" width="24"
         height="24" viewBox="0 0 24 24" strokeWidth="2.0" stroke="currentColor" fill="none"
         strokeLinecap="round" strokeLinejoin="round">
        <path stroke="none" d="M0 0h24v24H0z" fill="none"/>
        <path d="M18 6l-12 12"/>
        <path d="M6 6l12 12"/>
    </svg>
)