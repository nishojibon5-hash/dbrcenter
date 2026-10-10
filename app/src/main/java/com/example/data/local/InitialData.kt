package com.example.data.local

import com.example.data.model.*

object InitialData {
    val defaultServices = listOf(
        ServiceItemEntity(
            id = 1,
            title = "ডেন্ট পেইন্ট",
            description = "গাড়ির বডি ঠিক করা ও রঙ করার কাজ",
            iconKey = "dent",
            category = "বডি ও পেইন্ট",
            estimatedPrice = "৳ ২,৫০০ থেকে শুরু",
            sortOrder = 1
        ),
        ServiceItemEntity(
            id = 2,
            title = "ওয়াশ (ধোয়া-মোছা)",
            description = "গাড়ি পরিষ্কার ও চকচকে করার কার সার্ভিস",
            iconKey = "wash",
            category = "ক্লিনিং",
            estimatedPrice = "৳ ৫০০ থেকে শুরু",
            sortOrder = 2
        ),
        ServiceItemEntity(
            id = 3,
            title = "ইঞ্জিন সার্ভিস",
            description = "ইঞ্জিনের সম্পূর্ণ সার্ভিস ও মেরামত",
            iconKey = "engine",
            category = "মেকানিক্যাল",
            estimatedPrice = "আলোচনা সাপেক্ষে",
            sortOrder = 3
        ),
        ServiceItemEntity(
            id = 4,
            title = "গিয়ার বক্স সার্ভিস",
            description = "গিয়ার বক্স মেরামত ও সার্ভিস",
            iconKey = "gearbox",
            category = "মেকানিক্যাল",
            estimatedPrice = "আলোচনা সাপেক্ষে",
            sortOrder = 4
        ),
        ServiceItemEntity(
            id = 5,
            title = "হুইল ব্যালান্সিং",
            description = "চাকার ব্যালান্সিং ও অ্যালাইনমেন্ট",
            iconKey = "wheel",
            category = "হুইল",
            estimatedPrice = "৳ ৫০০ + ওয়েট",
            sortOrder = 5
        ),
        ServiceItemEntity(
            id = 6,
            title = "২ হুইলার ব্রেক সার্ভিস",
            description = "বাইক/মোটরসাইকেলের ব্রেক সার্ভিস",
            iconKey = "bike",
            category = "২ হুইলার",
            estimatedPrice = "৳ ৩৫০ থেকে শুরু",
            sortOrder = 6
        ),
        ServiceItemEntity(
            id = 7,
            title = "ফুল ফুয়েল সিস্টেম সার্ভিস",
            description = "ফুয়েল সিস্টেম ক্লিনিং ও সার্ভিস",
            iconKey = "fuel",
            category = "ফুয়েল",
            estimatedPrice = "৳ ১,৮০০ থেকে শুরু",
            sortOrder = 7
        ),
        ServiceItemEntity(
            id = 8,
            title = "কম্পিউটার স্ক্যান",
            description = "গাড়ির সমস্যা নির্ণয় (আলোচনা সাপেক্ষে)",
            iconKey = "scan",
            category = "ইলেকট্রনিক্স",
            estimatedPrice = "৳ ১,০০০ ফিক্সড",
            sortOrder = 8
        ),
        ServiceItemEntity(
            id = 9,
            title = "হাইব্রিড ব্যাটারি সার্ভিস",
            description = "হাইব্রিড গাড়ির ব্যাটারি সার্ভিস",
            iconKey = "battery",
            category = "হাইব্রিড",
            estimatedPrice = "আলোচনা সাপেক্ষে",
            sortOrder = 9
        ),
        ServiceItemEntity(
            id = 10,
            title = "হাইব্রিড কার অল ওয়ার্ক",
            description = "হাইব্রিড গাড়ির সকল কাজ",
            iconKey = "hybrid_car",
            category = "হাইব্রিড",
            estimatedPrice = "প্যাকেজ রেট",
            sortOrder = 10
        )
    )

    val defaultBanners = listOf(
        BannerAdEntity(
            id = 1,
            title = "গ্রীষ্মকালীন স্পেশাল কার কেয়ার অফার!",
            subtitle = "এসি কুলিং চেক ও সম্পূর্ণ কম্পিউটার স্ক্যানে পাচ্ছেন বিশেষ ছাড়!",
            badgeText = "২০% ডিসকাউন্ট",
            ctaText = "অফারটি গ্রহণ করুন",
            actionUrl = "offer_summer",
            placement = "middle",
            bgHex = "#FF6600"
        ),
        BannerAdEntity(
            id = 2,
            title = "ফ্রি কার হেলথ চেকআপ প্যাকেজ!",
            subtitle = "যেকোনো মেজর সার্ভিসের সাথে পাচ্ছেন ২৫টি গুরুত্বপূর্ণ চেকিং একদম ফ্রি।",
            badgeText = "সীমিত সময়ের ক্যাম্পেইন",
            ctaText = "এখনই অ্যাপয়েন্টমেন্ট নিন",
            actionUrl = "free_checkup",
            placement = "bottom",
            bgHex = "#073B34"
        )
    )

    val defaultWorkShowcases = listOf(
        WorkShowcaseEntity(
            id = 1,
            title = "টয়োটা এক্সিও ফুল বডি ডেন্টিং ও ওভেন পেইন্ট",
            categoryTag = "ডেন্টিং ও পেইন্ট",
            description = "অ্যাক্সিডেন্ট ক্ষতিগ্রস্ত ফেন্ডার ও ডোর মেটাল শেপিং এবং আধুনিক হিট চেম্বারে ১০০% ফ্যাক্টরি গ্রেড কালার স্প্রে ফিনিশিং।",
            imageType = "dent",
            imageUrl = "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?w=800",
            carModel = "Toyota Axio 2018",
            completionDuration = "৩ দিন"
        ),
        WorkShowcaseEntity(
            id = 2,
            title = "টয়োটা প্রিয়াস হাইব্রিড ব্যাটারি ব্যালান্সিং",
            categoryTag = "হাইব্রিড সিস্টেম",
            description = "ব্যাটারি ওভারহিটিং সমাধান, ২৮টি মডিউল স্বতন্ত্র সেল ভোল্টেজ ব্যালান্সিং, ফ্যান ডিপ ক্লিনিং ও হাইব্রিড ইনভার্টার ফ্লুইড সার্ভিস।",
            imageType = "engine",
            imageUrl = "https://images.unsplash.com/photo-1580273916550-e323be2ae537?w=800",
            carModel = "Toyota Prius Hybrid",
            completionDuration = "১ দিন"
        ),
        WorkShowcaseEntity(
            id = 3,
            title = "হোন্ডা ভেসেল অটো গিয়ারবক্স ক্লাচ সার্ভিস",
            categoryTag = "গিয়ারবক্স সার্ভিস",
            description = "ডুয়েল ক্লাচ জুডার ভাইব্রেশন ফিক্স, অরিজিনাল হোন্ডা DW-1 ফুল সিন্থেটিক অয়েল চেঞ্জ এবং ডিজিটাল ওবিডি ক্যালিব্রেশন।",
            imageType = "gearbox",
            imageUrl = "https://images.unsplash.com/photo-1486006920555-c77dce18193b?w=800",
            carModel = "Honda Vezel RS Hybrid",
            completionDuration = "১ দিন"
        ),
        WorkShowcaseEntity(
            id = 4,
            title = "মিতসুবিশি পাজেরো ইঞ্জিন ওভারহোলিং ও টিউনিং",
            categoryTag = "ইঞ্জিন ওভারহোল",
            description = "ইঞ্জিন কম্প্রেশন পুনরুদ্ধার, টাইমিং বেল্ট ও হেড গ্যাসকেট রিপ্লেসমেন্ট, ইনজেক্টর ক্লিনিং ও ইসিইউ ডিজিটাল ফাইন টিউনিং।",
            imageType = "engine",
            imageUrl = "https://images.unsplash.com/photo-1517524008697-84bbe3c3fd98?w=800",
            carModel = "Mitsubishi Pajero V6",
            completionDuration = "৪ দিন"
        ),
        WorkShowcaseEntity(
            id = 5,
            title = "টয়োটা নোয়া সাসপেনশন ও ৩D হুইল অ্যালাইনমেন্ট",
            categoryTag = "সাসপেনশন ও হুইল",
            description = "সামনের ও পেছনের ৪টি শক অ্যাবজরবার, লোয়ার আর্ম বুশ পরিবর্তন এবং লেজার ৩ডি অ্যালাইনমেন্ট ও ডিজিটাল হুইল ব্যালান্সিং।",
            imageType = "suspension",
            imageUrl = "https://images.unsplash.com/photo-1605559424843-9e4c228bf1c2?w=800",
            carModel = "Toyota Noah Esquire",
            completionDuration = "১ দিন"
        ),
        WorkShowcaseEntity(
            id = 6,
            title = "নিসান এক্স-ট্রেইল এসি কুলিং চেম্বার ও কার ওয়াশ",
            categoryTag = "এসি ও কার ওয়াশ",
            description = "কুলিং চেম্বার গ্যাস লিকেজ সমাধান, নতুন ইভাপোরেটর কয়েল ইন্সটলেশন, অরিজিনাল R134a গ্যাস চার্জিং ও প্রিমিয়াম স্নো ফোম ওয়াশ।",
            imageType = "wash",
            imageUrl = "https://images.unsplash.com/photo-1563720223185-11003d516935?w=800",
            carModel = "Nissan X-Trail T32",
            completionDuration = "১ দিন"
        )
    )

    val defaultReviews = listOf(
        CustomerReviewEntity(
            id = 1,
            customerName = "মো: রফিক উদ্দিন",
            carModelOrRole = "Toyota Premio",
            rating = 5,
            comment = "“DBR কার ওয়ার্কশপে আমার গাড়ির ডেন্ট পেইন্ট কাজটি খুবই ভালো হয়েছে। সেবা ও ব্যবহার অসাধারণ!”",
            avatarColorHex = "#0A5C4A",
            serviceTaken = "ডেন্ট পেইন্ট",
            isVerified = true,
            googleMapsProfile = "https://maps.google.com/?cid=dbr_bogra",
            isGoogleContributor = true
        ),
        CustomerReviewEntity(
            id = 2,
            customerName = "রাহাত ইসলাম",
            carModelOrRole = "Honda Grace Hybrid",
            rating = 5,
            comment = "“সার্ভিস খুবই ভালো। সময়মতো কাজ করে দেয়। দামও ন্যায্য। ধন্যবাদ DBR!”",
            avatarColorHex = "#FF6600",
            serviceTaken = "ইঞ্জিন সার্ভিস",
            isVerified = true,
            googleMapsProfile = "https://maps.google.com/?cid=dbr_bogra",
            isGoogleContributor = true
        ),
        CustomerReviewEntity(
            id = 3,
            customerName = "নাসরিন আক্তার",
            carModelOrRole = "Toyota Axio",
            rating = 5,
            comment = "“ডেন্ট পেইন্টর কাজটা অসাধারণ। গাড়ি এখন নতুনের মতো লাগছে!”",
            avatarColorHex = "#073B34",
            serviceTaken = "ডেন্ট পেইন্ট ও পলিশ",
            isVerified = true,
            googleMapsProfile = "https://maps.google.com/?cid=dbr_bogra",
            isGoogleContributor = true
        ),
        CustomerReviewEntity(
            id = 4,
            customerName = "মো: আরিফ হাসান",
            carModelOrRole = "Nissan X-Trail",
            rating = 5,
            comment = "“ইঞ্জিন সার্ভিসে অনেক যত্নশীল। এখন গাড়ি অনেক ভালো চলছে!”",
            avatarColorHex = "#0A5C4A",
            serviceTaken = "কম্পিউটার স্ক্যান ও ইঞ্জিন টিউন",
            isVerified = true,
            googleMapsProfile = "https://maps.google.com/?cid=dbr_bogra",
            isGoogleContributor = true
        ),
        CustomerReviewEntity(
            id = 5,
            customerName = "সাবিরা রহমান",
            carModelOrRole = "Toyota Allion",
            rating = 5,
            comment = "“খুবই পেশাদার সার্ভিস। আধুনিক মেশিন দিয়ে কাজ করে। আমি নিশ্চিতভাবে আবার আসব!”",
            avatarColorHex = "#FF6600",
            serviceTaken = "সাসপেনশন ও ব্রেক সার্ভিস",
            isVerified = true,
            googleMapsProfile = "https://maps.google.com/?cid=dbr_bogra",
            isGoogleContributor = true
        ),
        CustomerReviewEntity(
            id = 6,
            customerName = "তানভীর আহমেদ",
            carModelOrRole = "Toyota Fielder",
            rating = 5,
            comment = "“গিয়ার বক্সের ঝাঁকুনি সমস্যা বগুড়ার কোথাও সমাধান পাচ্ছিলাম না, DBR-এর টেকনিশিয়ানরা এক দিনেই নিখুঁত সমাধান করেছে।”",
            avatarColorHex = "#0E7490",
            serviceTaken = "গিয়ার বক্স ওভারহলিং",
            isVerified = true,
            googleMapsProfile = "https://maps.google.com/?cid=dbr_bogra",
            isGoogleContributor = true
        ),
        CustomerReviewEntity(
            id = 7,
            customerName = "ইঞ্জিনিয়ার কামরুল",
            carModelOrRole = "Mitsubishi Pajero",
            rating = 4,
            comment = "“সার্ভিস কোয়ালিটি অনেক ভালো ও দক্ষ টিম। ওয়েটিং লাউঞ্জের ব্যবস্থা চমৎকার।”",
            avatarColorHex = "#047857",
            serviceTaken = "হুইল ব্যালান্সিং ও কার ওয়াশ",
            isVerified = true,
            googleMapsProfile = "https://maps.google.com/?cid=dbr_bogra",
            isGoogleContributor = true
        )
    )

    val defaultBlogPosts = listOf(
        BlogPostEntity(
            id = "post-1",
            title = "গাড়ির ইঞ্জিনের আয়ু বাড়ানোর ৫টি জরুরি টিপস",
            summary = "নিয়মিত ইঞ্জিন অয়েল পরিবর্তন এবং কম্পিউটার ডায়াগনস্টিক কীভাবে আপনার গাড়ির খরচ ৫০% কমিয়ে দেয়।",
            content = "গাড়ির হৃদপিণ্ড হলো এর ইঞ্জিন। নিয়মিত ইঞ্জিন অয়েল ও ফিল্টার পরিবর্তন করা অত্যন্ত জরুরি। ভালো গ্রেডের সিন্থেটিক অয়েল ব্যবহার করলে ইঞ্জিন মসৃণ থাকে এবং জ্বালানি সাশ্রয় হয়। এছাড়া প্রতি ১০,০০০ কিমিতে কম্পিউটার ডায়াগনস্টিক স্ক্যান করালে অভ্যন্তরীণ যেকোনো ত্রুটি আগে থেকেই ধরা পড়ে।",
            category = "ইঞ্জিন কেয়ার",
            author = "হেড মেকানিক DBR",
            readTimeMinutes = 3,
            createdAt = System.currentTimeMillis() - 86400000L * 2
        ),
        BlogPostEntity(
            id = "post-2",
            title = "বর্ষাকালে গাড়ির ডেন্ট ও কালার সুরক্ষার উপায়",
            summary = "বৃষ্টির পানি ও কাদা থেকে গাড়ির বডিকে মরিচা মুক্ত রাখতে হিট চেম্বার পেইন্টিং ও সিরামিক কোটিংয়ের গুরুত্ব।",
            content = "বর্ষার সময় বাতাসে আর্দ্রতা বেশি থাকায় যেকোনো ছোট স্ক্র্যাচ বা ডেন্টে দ্রুত মরিচা ধরে যায়। DBR Complete Auto Centre-এ আধুনিক হিট চেম্বারে অরিজিনাল কালার ম্যাচিং করে ডেন্ট পেইন্ট করা হয়, যা বছরের পর বছর গাড়ির রং নতুনের মতো রাখে।",
            category = "ডেন্ট ও পেইন্ট",
            author = "পেইন্ট এক্সপার্ট DBR",
            readTimeMinutes = 4,
            createdAt = System.currentTimeMillis() - 86400000L * 5
        ),
        BlogPostEntity(
            id = "post-3",
            title = "অটোমেটিক ও CVT গিয়ারবক্স ফ্লুইড কখন বদলাবেন?",
            summary = "গিয়ার শিফটিংয়ে ঝাঁকুনি বা লেট রেসপন্স? জানুন গিয়ারবক্স দীর্ঘস্থায়ী রাখার সঠিক নিয়ম।",
            content = "সিভিটি এবং অটোমেটিক ট্রান্সমিশনের জন্য নির্দিষ্ট গ্রেডের ফ্লুইড প্রয়োজন। প্রতি ৩০,০০০ থেকে ৪০,০০০ কিলোমিটারে গিয়ার অয়েল পরিবর্তন না করলে গিয়ার বক্স গরম হয়ে বড় ধরনের ক্ষতির আশঙ্কা থাকে। নিয়মিত চেকআপ করুন।",
            category = "গিয়ারবক্স",
            author = "টেকনিক্যাল টিম DBR",
            readTimeMinutes = 3,
            createdAt = System.currentTimeMillis() - 86400000L * 8
        )
    )

    val defaultSiteSettings = SiteSettingsEntity()

    val defaultMechanics = listOf(
        MechanicEntity(
            id = "mech_1",
            name = "মো: রফিকুল ইসলাম",
            role = "হেড টেকনিশিয়ান ও EFI স্পেশালিস্ট",
            experience = "১২+ বছর কাজের অভিজ্ঞতা",
            phone = "01776-596315",
            imageUrl = "https://images.unsplash.com/photo-1581092160607-ee22621dd758?w=600",
            skills = listOf("ইঞ্জিন ওভারহলিং", "ইএফআই কম্পিউটার স্ক্যান", "অটো গিয়ারবক্স", "হাইব্রিড সিস্টেম"),
            bio = "বগুড়াসহ উত্তরবঙ্গের স্বনামধন্য বিভিন্ন কার ব্র্যান্ডের অফিসিয়াল সার্ভিস সেন্টারে দীর্ঘ ১২ বছর সফলতার সাথে কাজ করেছেন। ইঞ্জিনের সুক্ষ্মতম শব্দ শুনেও ত্রুটি নির্ণয়ে পারদর্শী।",
            works = listOf(
                "https://images.unsplash.com/photo-1486006920555-c77dce18193b?w=400",
                "https://images.unsplash.com/photo-1617814076367-b759c7d7e738?w=400"
            )
        ),
        MechanicEntity(
            id = "mech_2",
            name = "ওস্তাদ আনোয়ার হোসেন",
            role = "ডেন্টিং ও পেইন্ট মাস্টার",
            experience = "১৫+ বছর কাজের অভিজ্ঞতা",
            phone = "01711-287258",
            imageUrl = "https://images.unsplash.com/photo-1619642751034-765dfdf7c58e?w=600",
            skills = listOf("ফুল বডি কালার", "মাইক্রো ডেন্টিং", "বেকিং পেইন্ট বুথ", "সিরামিক পলিশ"),
            bio = "গাড়ির রঙ শোরুম কন্ডিশন ফিরিয়ে আনার জন্য বগুড়ার অন্যতম অভিজ্ঞ পেইন্টার। কম্পিউটারাইজড কালার ম্যাচিং এবং হাই-গ্লস ল্যাকার ফিনিশিংয়ে শতভাগ সন্তুষ্টি প্রদান করেন।",
            works = listOf(
                "https://images.unsplash.com/photo-1503376780353-7e6692767b70?w=400",
                "https://images.unsplash.com/photo-1563720223185-11003d516935?w=400"
            )
        ),
        MechanicEntity(
            id = "mech_3",
            name = "ইঞ্জিনিয়ার জাহিদুল ইসলাম",
            role = "হাইব্রিড ও ইলেকট্রিক ডায়াগনস্টিক এক্সপার্ট",
            experience = "৮+ বছর কাজের অভিজ্ঞতা",
            phone = "01776-596315",
            imageUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=600",
            skills = listOf("হাইব্রিড ব্যাটারি সেল ব্যালান্সিং", "ইনভার্টার কুল্যান্ট সার্ভিস", "ইসিইউ প্রোগ্রামিং", "এবিএস মডিউল"),
            bio = "টয়োটা প্রিয়াস, অ্যাক্সেল, হোন্ডা গ্রেস এবং হারিয়ার হাইব্রিড সিস্টেমের সফটওয়্যার ও ব্যাটারি রিপেয়ারে বিশেষ প্রশিক্ষণপ্রাপ্ত ডিপ্লোমা ইঞ্জিনিয়ার।",
            works = listOf(
                "https://images.unsplash.com/photo-1517524008697-84bbe3c3fd98?w=400"
            )
        )
    )
}
